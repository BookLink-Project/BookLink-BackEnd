package BookLink.BookLink.Service.Community.BookReport;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Community.BookReport.*;
import BookLink.BookLink.Domain.Community.BookReport.BookReportDetailDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportRepliesDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.Community.BookReport.BookReportLikeRepository;
import BookLink.BookLink.Repository.Community.BookReport.BookReportRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReport.BookReportReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReport.BookReportReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookReportServiceImpl implements BookReportService {

    private final MemberRepository memberRepository;
    private final BookReportRepository bookReportRepository;
    private final BookReportLikeRepository bookReportLikeRepository;
    private final BookReportReplyRepository bookReportReplyRepository;
    private final BookReportReplyLikeRepository bookReportReplyLikeRepository;
    private final BookRepository bookRepository;

    @Override
    public ResponseDto writeReport(BookReportDto.Request requestDto, Member loginMember) {

        BookReport bookReport = BookReportDto.Request.toEntity(requestDto, loginMember);
        bookReportRepository.save(bookReport);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("정상 등록 완료");

        return responseDto;

    }

    @Override
    public ResponseDto reportList() {

        ResponseDto responseDto = new ResponseDto();

        List<BookReport> all_report = bookReportRepository.findAll();
        List<BookReportDto.Response> report_response = new ArrayList<>();

        for (int i = 0; i < all_report.size(); i++) {
            BookReportDto.Response response = BookReportDto.Response.toDto(all_report.get(i));
            report_response.add(i, response);
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("독후감 목록 조회입니다.");
        responseDto.setData(report_response);

        return responseDto;
    }

    //
    @Override
    @Transactional
    public ResponseDto reportDetail(Long id, MemberPrincipal memberPrincipal) {

        Member loginMember = (memberPrincipal == null) ? null : memberPrincipal.getMember();

        ResponseDto responseDto = new ResponseDto();

        BookReport post = bookReportRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        post.view_plus(); // 조회수 증가

        boolean isLiked = (loginMember != null) && bookReportLikeRepository.existsByMemberAndPost(loginMember, post);

        // 댓글 조회
        List<BookReportReply> replyList = bookReportReplyRepository.findByPostOrderByParentDescIdDesc(post);

        List<BookReportRepliesDto> replies = new ArrayList<>();

        for (BookReportReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            Long sub_reply_cnt = parentId.equals(replyId) ? bookReportReplyRepository.countByParentId(parentId) - 1 : 0; // 대댓글 수

            // 좋아요 상태
            boolean isLikedReply = (loginMember != null) && bookReportReplyLikeRepository.existsByMemberAndReply(loginMember, reply);

            BookReportRepliesDto rv;

            rv = new BookReportRepliesDto(
                    replyId,
                    parentId,
                    writer.getNickname(),
                    reply.getContent(),
                    reply.getCreatedTime(),
                    writer.getImage(),
                    reply.getLike_cnt(),
                    sub_reply_cnt,
                    isLikedReply,
                    reply.isUpdated()
            );
            replies.add(rv);
        }

        //
        BookReportDetailDto result = new BookReportDetailDto(
                post.getBook_title(),
                post.getIsbn(),
                post.getAuthors(),
                post.getPublisher(),
                post.getPud_date(),
                post.getCover(),
                post.getCategory(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedTime(),
                post.getWriter().getNickname(),
                post.getWriter().getImage(),
                post.getView_cnt(),
                post.getLike_cnt(),
                post.getReply_cnt(),
                isLiked,
                post.isUpdated(),
                replies
        );
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto reportUpdate(Long id, BookReportUpdateDto bookReportUpdateDto) {

        ResponseDto responseDto = new ResponseDto();

        BookReport updatePost = bookReportRepository.findById(id).orElse(null);

        if (updatePost == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        String newTitle = bookReportUpdateDto.getTitle();
        String newContent = bookReportUpdateDto.getContent();

        updatePost.updatePost(newTitle, newContent);

        responseDto.setStatus(HttpStatus.CREATED);

        bookReportUpdateDto.setTitle(newTitle);
        bookReportUpdateDto.setContent(newContent);
        responseDto.setData(bookReportUpdateDto);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likePost(Long id, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookReport post = bookReportRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글입니다");
            return responseDto;
        }

        BookReportLike bookReportLike = bookReportLikeRepository.findByMemberAndPost(loginMember, post).orElse(null);

        if (bookReportLike != null) { // 좋아요 눌린 상태
            bookReportLikeRepository.delete(bookReportLike);
            post.like_minus();

            responseDto.setMessage("좋아요 취소 성공");
        } else { // 좋아요 안눌린 상태
            bookReportLike = BookReportLike.builder()
                    .post(post)
                    .member(loginMember)
                    .build();

            bookReportLikeRepository.save(bookReportLike);
            post.like_plus();

            responseDto.setMessage("좋아요 성공");
        }

        BookReportLikeDto bookReportLikeDto = new BookReportLikeDto(post.getLike_cnt());
        responseDto.setData(bookReportLikeDto);

        return responseDto;
    }

    @Override
    public ResponseDto deletePost(Long id) {

        ResponseDto responseDto = new ResponseDto();

        BookReport post = bookReportRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        bookReportRepository.deleteById(id);

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }


}

