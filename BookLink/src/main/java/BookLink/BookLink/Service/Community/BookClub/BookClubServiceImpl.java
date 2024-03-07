package BookLink.BookLink.Service.Community.BookClub;

import BookLink.BookLink.Domain.Community.BookClub.*;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubRepliesDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookClub.BookClubLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClub.BookClubReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClub.BookClubReplyRepository;
import BookLink.BookLink.Repository.Community.BookClub.BookClubRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookClubServiceImpl implements BookClubService {

    private final BookClubRepository bookClubRepository;
    private final BookClubLikeRepository bookClubLikeRepository;
    private final BookClubReplyRepository bookClubReplyRepository;
    private final BookClubReplyLikeRepository bookClubReplyLikeRepository;

    @Override
    public ResponseDto writePost(Member loginMember, BookClubDto.Request bookClubDto) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookClub bookClub = bookClubDto.toEntity(loginMember);

        try {

            bookClubRepository.save(bookClub);

        } catch (Exception ex) {

            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("입력 미완료");

            return responseDto;
        }

        return responseDto;
    }

    @Override
    public ResponseDto listPost() {

        ResponseDto responseDto = new ResponseDto();

        List<BookClub> bookClubList = bookClubRepository.findAll();
        List<BookClubDto.Response> responseData = new ArrayList<>();

        for (BookClub bookClub : bookClubList) {

            BookClubDto.Response response = BookClubDto.Response.builder()
                    .id(bookClub.getId())
                    .title(bookClub.getTitle())
                    .writer(bookClub.getWriter().getNickname())
                    .content(bookClub.getContent())
                    .location(bookClub.getLocation())
                    .date(bookClub.getCreatedTime())
                    .reply_cnt(bookClub.getReply_cnt())
                    .build();

            responseData.add(response);
        }
        responseDto.setData(responseData);

        return responseDto;

    }

    @Override
    @Transactional
    public ResponseDto showPost(MemberPrincipal memberPrincipal, Long id) {

        Member loginMember = (memberPrincipal == null) ? null : memberPrincipal.getMember();

        ResponseDto responseDto = new ResponseDto();

        BookClub post = bookClubRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        post.increaseViewCnt();

        boolean isLiked = (loginMember != null)
                && (bookClubLikeRepository.existsByMemberAndPost(loginMember, post));

        // START 댓글 조회
        List<BookClubReply> replyList = bookClubReplyRepository.findByPostOrderByParentDescIdDesc(post);

        List<BookClubRepliesDto> replies = new ArrayList<>();

        for (BookClubReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            // 대댓글 수 (부모 : 자식)
            Long sub_reply_cnt = parentId.equals(replyId) ? bookClubReplyRepository.countByParentId(parentId) - 1 : 0;

            // 좋아요 상태
            boolean isLikedReply = (loginMember != null)
                    && (bookClubReplyLikeRepository.existsByMemberAndReply(loginMember, reply));

            BookClubRepliesDto rv;

            rv = new BookClubRepliesDto(
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
        // END 댓글 조회

        BookClubDetailDto result = new BookClubDetailDto(
                post.getTitle(),
                post.getLocation(),
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
    public ResponseDto modifyPost(Long id, BookClubUpdateDto bookClubDto) {

        ResponseDto responseDto = new ResponseDto();

        BookClub updatePost = bookClubRepository.findById(id).orElse(null);

        if (updatePost == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        String newTitle = bookClubDto.getTitle();
        String newContent = bookClubDto.getContent();

        updatePost.updatePost(newTitle, newContent);

        responseDto.setStatus(HttpStatus.CREATED);

        bookClubDto.setTitle(newTitle);
        bookClubDto.setContent(newContent);
        responseDto.setData(bookClubDto);

        return responseDto;
    }

    @Override
    public ResponseDto deletePost(Long id) {

        ResponseDto responseDto = new ResponseDto();

        BookClub post = bookClubRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        bookClubRepository.deleteById(id);

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likePost(Member loginMember, Long id) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookClub post = bookClubRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        BookClubLike bookClubLike = bookClubLikeRepository.findByMemberAndPost(loginMember, post).orElse(null);

        if (bookClubLike == null) { // 좋아요 안 눌린 상태

            bookClubLike = BookClubLike.builder()
                    .post(post)
                    .member(loginMember)
                    .build();

            bookClubLikeRepository.save(bookClubLike);
            post.increaseLikeCnt();

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            bookClubLikeRepository.delete(bookClubLike);
            post.decreaseLikeCnt();

            responseDto.setMessage("좋아요 취소 성공");
        }

        BookClubLikeDto bookClubLikeDto = new BookClubLikeDto(post.getLike_cnt());
        responseDto.setData(bookClubLikeDto);

        return responseDto;

    }
}
