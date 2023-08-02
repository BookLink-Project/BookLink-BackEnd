package BookLink.BookLink.Controller.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardUpdateDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyUpdateDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.BookClubService;
import BookLink.BookLink.Service.Community.BookReportService;
import BookLink.BookLink.Service.Community.FreeBoardService;
import BookLink.BookLink.Service.CommunityReply.BookClubReplyService;
import BookLink.BookLink.Service.CommunityReply.BookReportReplyService;
import BookLink.BookLink.Service.CommunityReply.FreeBoardReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;
    private final FreeBoardReplyService freeBoardReplyService;

    @GetMapping() // 커뮤니티 홈
    public ResponseDto communityHome() {
        return null;
    }

    @GetMapping(value = {"/board/{category}", "/board"}) // 게시판 리스트 (자유글 + 독후감)
    public ResponseDto listBoard(@PathVariable String category) {
        return null;
    }

    @PostMapping("/board/free") // 자유글 작성
    public ResponseEntity<ResponseDto> writeFreeBoard (@RequestBody FreeBoardDto.Request freeBoardDto,
                                       @AuthenticationPrincipal Member member) {

        ResponseDto responseDto = freeBoardService.writePost(member.getEmail(), freeBoardDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @GetMapping("/board/free")
    public ResponseEntity<ResponseDto> freeBoardList() {

        ResponseDto responseDto = freeBoardService.freeBoardList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/board/free/{id}")
    public ResponseEntity<ResponseDto> freeBoardDetail(@PathVariable Long id, @AuthenticationPrincipal String memEmail) {
        ResponseDto responseDto = freeBoardService.freeBoardDetail(id, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping("/board/free/{id}")
    public ResponseEntity<ResponseDto> freeBoardUpdate(@PathVariable Long id, @RequestBody FreeBoardUpdateDto requestDto) {
        ResponseDto responseDto = freeBoardService.freeBoardUpdate(id, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @DeleteMapping("/board/free/{id}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long id) {
        ResponseDto responseDto = freeBoardService.deletePost(id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/free/{id}/like")
    public ResponseEntity<ResponseDto> likePost(@PathVariable Long id, @AuthenticationPrincipal String memEmail) {
        ResponseDto responseDto = freeBoardService.likePost(id, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/free/{id}")
    public ResponseEntity<ResponseDto> writeReply(@AuthenticationPrincipal String memEmail,
                                                  @PathVariable Long id, @RequestBody FreeBoardReplyDto.Request requestDto) {
        ResponseDto responseDto = freeBoardReplyService.writeReply(memEmail, id, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping("/board/free/{id}/{replyId}")
    public ResponseEntity<ResponseDto> updateReply(@PathVariable Long id, @PathVariable Long replyId,
                                                   @RequestBody FreeBoardReplyUpdateDto requestDto) {

        ResponseDto responseDto = freeBoardReplyService.updateReply(id, replyId, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @DeleteMapping("/board/free/{id}/{replyId}")
    public ResponseEntity<ResponseDto> deleteReply(@PathVariable Long id, @PathVariable Long replyId) {
        ResponseDto responseDto = freeBoardReplyService.deleteReply(id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/free/{id}/{replyId}/like")
    public ResponseEntity<ResponseDto> likeReply(@AuthenticationPrincipal String memEmail, @PathVariable Long id,
                                                 @PathVariable Long replyId) {

        ResponseDto responseDto = freeBoardReplyService.likeReply(memEmail, id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
