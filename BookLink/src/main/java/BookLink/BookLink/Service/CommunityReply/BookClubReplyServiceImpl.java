package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.CommunityReply.BookClubReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClubReplyRepository;
import BookLink.BookLink.Repository.Community.BookClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookClubReplyServiceImpl implements BookClubReplyService{

    private final BookClubRepository bookClubRepository;
    private final BookClubReplyRepository bookClubReplyRepository;
    private final BookClubReplyLikeRepository bookClubReplyLikeRepository;


    @Override
    @Transactional
    public ResponseDto writeReply(Member loginMember, Long postId, BookClubReplyDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookClub post = bookClubRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        BookClubReply savedReply;

        if (replyDto.getParentId() != 0) { // 자식 댓글의 경우

            BookClubReply parent = bookClubReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            BookClubReply bookClubReply = replyDto.toEntity(post, loginMember, parent);
            savedReply = bookClubReplyRepository.save(bookClubReply);

        } else { // 부모 댓글의 경우

            BookClubReply bookClubReply = replyDto.toEntity(post, loginMember, null);
            savedReply = bookClubReplyRepository.save(bookClubReply);

            // dirty checking
            BookClubReply updateReply = bookClubReplyRepository.findById(bookClubReply.getId()).orElse(new BookClubReply());
            updateReply.updateParent(savedReply);
        }
        post.increaseReplyCnt();

        BookClubReplyDto.Response responseData = new BookClubReplyDto.Response(
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
    public ResponseDto updateReply(Long postId, Long replyId, BookClubReplyUpdateDto replyDto) {

        ResponseDto responseDto = new ResponseDto();

        BookClubReply updateReply = bookClubReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

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

        BookClub post = bookClubRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        BookClubReply deleteReply = bookClubReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (deleteReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        Long parentId = deleteReply.getParent().getId();

        if (parentId.equals(replyId)) { // 부모 댓글의 경우

            Long delete_cnt = bookClubReplyRepository.countByParentId(replyId);
            System.out.println(delete_cnt);
            post.decreaseReplyCnt(delete_cnt);

            bookClubReplyRepository.deleteById(replyId);

        } else { // 자식 댓글의 경우

            bookClubReplyRepository.deleteById(replyId);

            post.decreaseReplyCnt(1L);

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
        if (!bookClubRepository.existsById(postId)) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        // TODO 글-댓글 매칭 안 될 경우 예외 처리

        BookClubReply reply = bookClubReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (reply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하지 않는 댓글");
            return responseDto;
        }

        BookClubReplyLike replyLike = bookClubReplyLikeRepository.findByMemberAndReply(loginMember, reply).orElse(null);

        if (replyLike == null) { // 좋아요 안 눌린 상태

            replyLike = BookClubReplyLike.builder()
                    .member(loginMember)
                    .reply(reply)
                    .build();

            bookClubReplyLikeRepository.save(replyLike);
            reply.increaseLikeCnt();

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            bookClubReplyLikeRepository.delete(replyLike);
            reply.decreaseLikeCnt();

            responseDto.setMessage("좋아요 취소 성공");
        }

        BookClubReplyLikeDto likeDto = new BookClubReplyLikeDto(reply.getLike_cnt());
        responseDto.setData(likeDto);

        return responseDto;
    }
}
