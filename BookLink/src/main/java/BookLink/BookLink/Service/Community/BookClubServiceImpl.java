package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.Community.BookClubDetailDto;
import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookClubRepliesDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookClubLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClubReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClubReplyRepository;
import BookLink.BookLink.Repository.Community.BookClubRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookClubServiceImpl implements BookClubService {

    private final MemberRepository memberRepository;
    private final BookClubRepository bookClubRepository;
    private final BookClubLikeRepository bookClubLikeRepository;
    private final BookClubReplyRepository bookClubReplyRepository;
    private final BookClubReplyLikeRepository bookClubReplyLikeRepository;

    @Override
    public ResponseDto writePost(String memEmail, BookClubDto.Request bookClubDto) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        BookClub bookClub = bookClubDto.toEntity(loginMember);

        try {

            bookClubRepository.save(bookClub);

        } catch (Exception ex) {

            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("입력 미완료");

            return responseDto;
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");

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
                    .reply_cnt(10L) // TODO dummy
                    .build();

            responseData.add(response);

        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("목록 조회 성공");
        responseDto.setData(responseData);

        return responseDto;

    }

    @Override
    public ResponseDto showPost(String memEmail, Long id) throws MalformedURLException {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        BookClub post = bookClubRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        boolean isLiked = bookClubLikeRepository.existsByMemberAndPost(loginMember, post);

        // START 댓글 조회
        List<BookClubReply> replyList = bookClubReplyRepository.findByPostOrderByParentDescIdDesc(post);

        List<BookClubRepliesDto> replies = new ArrayList<>();

        for (BookClubReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            // 대댓글 수 (부모 : 자식)
            Long sub_reply_cnt = parentId.equals(replyId) ? bookClubReplyRepository.countByParentId(parentId) - 1 : 0; // 대댓글 수

            // 좋아요 상태
            boolean isLikedReply = bookClubReplyLikeRepository.existsByMemberAndReply(loginMember, reply);

            BookClubRepliesDto rv;
            if (reply.isDeleted()) {

                rv = new BookClubRepliesDto(
                        replyId,
                        parentId,
                        "(삭제)",
                        "삭제된 댓글입니다.",
                        null,
                        null,
                        null,
                        sub_reply_cnt,
                        null,
                        null

                );
            } else {

                rv = new BookClubRepliesDto(
                        replyId,
                        parentId,
                        writer.getNickname(),
                        reply.getContent(),
                        reply.getCreatedTime(),
                        //writer.getImage(),
                        new URL("https://soccerquick.s3.ap-northeast-2.amazonaws.com/1689834239634.png"), // TODO dummy
                        reply.getLike_cnt(),
                        sub_reply_cnt,
                        isLikedReply,
                        reply.isUpdated()

                );
            }
            replies.add(rv);

        }
        // END 댓글 조회

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("글 조회 성공");

        BookClubDetailDto result = new BookClubDetailDto(

                post.getTitle(),
                post.getLocation(),
                post.getContent(),
                post.getCreatedTime(),
                post.getWriter().getNickname(),
                //post.getWriter().getImage(),
                new URL("https://soccerquick.s3.ap-northeast-2.amazonaws.com/1689834239634.png"), // TODO dummy
                post.getView_cnt(),
                post.getLike_cnt(),
                post.getReply_cnt(),
                isLiked,
                replies
        );
        responseDto.setData(result);

        return responseDto;
    }
}
