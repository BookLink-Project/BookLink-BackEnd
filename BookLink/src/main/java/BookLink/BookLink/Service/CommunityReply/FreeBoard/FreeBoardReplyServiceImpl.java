package BookLink.BookLink.Service.CommunityReply.FreeBoard;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.FreeBoard.FreeBoardRepository;
import BookLink.BookLink.Repository.CommunityReply.FreeBoard.FreeBoardReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.FreeBoard.FreeBoardReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreeBoardReplyServiceImpl implements FreeBoardReplyService{

    private final MemberRepository memberRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardReplyRepository freeBoardReplyRepository;
    private final FreeBoardReplyLikeRepository freeBoardReplyLikeRepository;

    @Override
    @Transactional
    public ResponseDto writeReply(Member loginMember, Long postId, FreeBoardReplyDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");

            return responseDto;
        }

        FreeBoard post = freeBoardRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글입니다.");

            return responseDto;
        }

        FreeBoardReply savedReply;

        if (replyDto.getParentId() != 0) { //자식댓글의 경우

            FreeBoardReply parent = freeBoardReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            FreeBoardReply freeBoardReply = replyDto.toEntity(post, loginMember, parent);
            savedReply = freeBoardReplyRepository.save(freeBoardReply);
        } else { // 부모댓글의 경우

            FreeBoardReply freeBoardReply = replyDto.toEntity(post, loginMember, null);
            savedReply = freeBoardReplyRepository.save(freeBoardReply);

            //dirty checking
            FreeBoardReply updateReply = freeBoardReplyRepository.findById(freeBoardReply.getId()).orElse(new FreeBoardReply());
            updateReply.updateParent(savedReply);
        }

        post.replyCnt_plus();

        FreeBoardReplyDto.Response responseData = new FreeBoardReplyDto.Response(
                savedReply.getId(),
                savedReply.getCreatedTime(),
                savedReply.getContent(),
                loginMember.getNickname(),
                loginMember.getImage()
        );
        responseDto.setData(responseData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateReply(Long postId, Long replyId, FreeBoardReplyUpdateDto replyDto) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoardReply updateReply = freeBoardReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (updateReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        updateReply.updateReply(replyDto.getContent());

        replyDto.setContent(updateReply.getContent());

        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setData(replyDto);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto deleteReply(Long postId, Long replyId) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoard post = freeBoardRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        FreeBoardReply deleteReply = freeBoardReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (deleteReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        Long parentId = deleteReply.getParent().getId();

        if (parentId.equals(replyId)) { // 부모 댓글의 경우
            Long delete_cnt = freeBoardReplyRepository.countByParentId(replyId);
            System.out.println(delete_cnt);
            post.replyCnt_minus(delete_cnt);
            freeBoardReplyRepository.deleteById(replyId);
        } else {
            freeBoardReplyRepository.deleteById(replyId);

            post.replyCnt_minus(1L);
        }

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likeReply(Member loginMember, Long postId, Long replyId) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        FreeBoard post = freeBoardRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        // TODO 글-댓글 매칭 안 될 경우 예외 처리

        FreeBoardReply reply = freeBoardReplyRepository.findById(replyId).orElse(null);

        if (reply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하지 않는 댓글");
            return responseDto;
        }

        FreeBoardReplyLike replyLike = freeBoardReplyLikeRepository.findByMemberAndReply(loginMember, reply).orElse(null);

        if (replyLike == null) { // 좋아요 안 눌린 상태

            replyLike = FreeBoardReplyLike.builder()
                    .member(loginMember)
                    .reply(reply)
                    .build();

            freeBoardReplyLikeRepository.save(replyLike);
            reply.increaseLikeCnt();

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            freeBoardReplyLikeRepository.delete(replyLike);
            reply.decreaseLikeCnt();

            responseDto.setMessage("좋아요 취소 성공");

        }
        FreeBoardReplyLikeDto likeDto = new FreeBoardReplyLikeDto(reply.getLike_cnt());
        responseDto.setData(likeDto);

        return responseDto;
    }
}
