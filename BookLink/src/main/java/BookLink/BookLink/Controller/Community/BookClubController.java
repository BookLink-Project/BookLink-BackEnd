package BookLink.BookLink.Controller.Community;

import BookLink.BookLink.Domain.Community.BookClub.BookClubDto;
import BookLink.BookLink.Domain.Community.BookClub.BookClubUpdateDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.BookClub.BookClubService;
import BookLink.BookLink.Service.CommunityReply.BookClub.BookClubReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities")
public class BookClubController {

    private final BookClubService bookClubService;
    private final BookClubReplyService bookClubReplyService;

    @PostMapping("/book-club") // 독서모임 글 작성
    public ResponseEntity<ResponseDto> writeBookClub (@RequestBody BookClubDto.Request bookClubDto,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookClubService.writePost(member, bookClubDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @GetMapping("/book-club") // 독서모임 글 목록 조회
    public ResponseEntity<ResponseDto> listBookClub() {

        ResponseDto responseDto = bookClubService.listPost();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @GetMapping("/book-club/{id}") // 독서모임 글 상세 조회
    public ResponseEntity<ResponseDto> showBookClub(@PathVariable Long id,
                                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookClubService.showPost(member, id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PatchMapping("/book-club/{id}") // 독서모임 글 수정
    public ResponseEntity<ResponseDto> modifyBookClub(@PathVariable Long id,
                                                      @RequestBody BookClubUpdateDto bookClubDto) {

        ResponseDto responseDto = bookClubService.modifyPost(id, bookClubDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @DeleteMapping("/book-club/{id}") // 독서모임 글 삭제
    public ResponseEntity<ResponseDto> deleteBookClub(@PathVariable Long id) {

        ResponseDto responseDto = bookClubService.deletePost(id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PostMapping("/book-club/{id}/like") // 독서모임 글 좋아요
    public ResponseEntity<ResponseDto> likeBookClub(@PathVariable Long id,
                                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookClubService.likePost(member, id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PostMapping("/book-club/{id}") // 독서모임 댓글 작성
    public ResponseEntity<ResponseDto> writeCookClubReply(@PathVariable Long id,
                                                          @RequestBody BookClubReplyDto.Request replyDto,
                                                          @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws MalformedURLException {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookClubReplyService.writeReply(member, id, replyDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PatchMapping("/book-club/{id}/{replyId}") // 독서모임 댓글 수정
    public ResponseEntity<ResponseDto> updateCookClubReply(@PathVariable Long id,
                                                           @PathVariable Long replyId,
                                                           @RequestBody BookClubReplyUpdateDto replyDto) {

        ResponseDto responseDto = bookClubReplyService.updateReply(id, replyId, replyDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @DeleteMapping("/book-club/{id}/{replyId}") // 독서모임 댓글 삭제
    public ResponseEntity<ResponseDto> deleteCookClubReply(@PathVariable Long id,
                                                           @PathVariable Long replyId) {

        ResponseDto responseDto = bookClubReplyService.deleteReply(id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PostMapping("/book-club/{id}/{replyId}/like") // 독서모임 댓글 좋아요
    public ResponseEntity<ResponseDto> likeBookClubReply(@PathVariable Long id,
                                                         @PathVariable Long replyId,
                                                         @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookClubReplyService.likeReply(member, id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }
}
