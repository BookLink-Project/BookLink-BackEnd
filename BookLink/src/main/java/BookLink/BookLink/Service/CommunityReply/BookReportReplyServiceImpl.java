package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.BookReport.BookReportLikeDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReportReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReportReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReportReplyServiceImpl implements BookReportReplyService {

    private final MemberRepository memberRepository;
    private final BookReportRepository bookReportRepository;
    private final BookReportReplyRepository bookReportReplyRepository;
    private final BookReportReplyLikeRepository bookReportReplyLikeRepository;

    @Override
    @Transactional
    public ResponseDto writeReply(String memEmail, Long postId, BookReportReplyDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");

            return responseDto;
        }

        BookReport post = bookReportRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글입니다.");

            return responseDto;
        }

        BookReportReply savedReply;

        if (replyDto.getParentId() != 0) { //자식댓글의 경우

            BookReportReply parent = bookReportReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            BookReportReply bookReportReply = replyDto.toEntity(post, loginMember, parent);
            savedReply = bookReportReplyRepository.save(bookReportReply);
        } else { // 부모댓글의 경우

            BookReportReply bookReportReply = replyDto.toEntity(post, loginMember, null);
            savedReply = bookReportReplyRepository.save(bookReportReply);

            //dirty checking
            BookReportReply updateReply = bookReportReplyRepository.findById(bookReportReply.getId()).orElse(new BookReportReply());
            updateReply.updateParent(savedReply);
        }

        post.replyCnt_plus();

        BookReportReplyDto.Response responseData = new BookReportReplyDto.Response(
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
    public ResponseDto updateReply(Long postId, Long replyId, BookReportReplyUpdateDto replyDto) {

        ResponseDto responseDto = new ResponseDto();

        BookReportReply updateReply = bookReportReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

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

        BookReport post = bookReportRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        BookReportReply deleteReply = bookReportReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (deleteReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        Long parentId = deleteReply.getParent().getId();

        if (parentId.equals(replyId)) { // 부모 댓글의 경우
            Long delete_cnt = bookReportReplyRepository.countByParentId(replyId);
            System.out.println(delete_cnt);
            post.replyCnt_minus(delete_cnt);

            bookReportReplyRepository.deleteById(replyId);
        } else {

            bookReportReplyRepository.deleteById(replyId);

            post.replyCnt_minus(1L);
        }

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likeReply(String memEmail, Long postId, Long replyId) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookReport post = bookReportRepository.findById(postId).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        // TODO 글-댓글 매칭 안 될 경우 예외 처리

        BookReportReply reply = bookReportReplyRepository.findById(replyId).orElse(null);

        if (reply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하지 않는 댓글");
            return responseDto;
        }

        BookReportReplyLike replyLike = bookReportReplyLikeRepository.findByMemberAndReply(loginMember, reply).orElse(null);

        if (replyLike == null) { // 좋아요 안 눌린 상태

            replyLike = BookReportReplyLike.builder()
                    .member(loginMember)
                    .reply(reply)
                    .build();

            bookReportReplyLikeRepository.save(replyLike);
            reply.increaseLikeCnt();

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            bookReportReplyLikeRepository.delete(replyLike);
            reply.decreaseLikeCnt();

            responseDto.setMessage("좋아요 취소 성공");

        }
        BookReportReplyLikeDto likeDto = new BookReportReplyLikeDto(reply.getLike_cnt());
        responseDto.setData(likeDto);

        return responseDto;
    }
}
