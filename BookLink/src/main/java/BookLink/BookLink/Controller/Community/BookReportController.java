package BookLink.BookLink.Controller.Community;

import BookLink.BookLink.Domain.Community.BookReport.BookReportDto;
import BookLink.BookLink.Domain.Community.BookReport.BookReportUpdateDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.BookReportService;
import BookLink.BookLink.Service.CommunityReply.BookReportReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities")
public class BookReportController {

    private final BookReportService bookReportService;
    private final BookReportReplyService bookReportReplyService;

    @PostMapping("/board/report")
    public ResponseEntity<ResponseDto> writeReport(@RequestBody BookReportDto.Request requestDto,
                                                   @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = bookReportService.writeReport(requestDto, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @GetMapping("/board/report")
    public ResponseEntity<ResponseDto> bookReportList() {

        ResponseDto responseDto = bookReportService.reportList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/board/report/{id}")
    public ResponseEntity<ResponseDto> reportDetail(@PathVariable Long id, @AuthenticationPrincipal String memEmail) {
        ResponseDto responseDto = bookReportService.reportDetail(id, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping("/board/report/{id}")
    public ResponseEntity<ResponseDto> reportUpdate(@PathVariable Long id, @RequestBody BookReportUpdateDto requestDto) {
        ResponseDto responseDto = bookReportService.reportUpdate(id, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/report/{id}/like")
    public ResponseEntity<ResponseDto> likePost(@PathVariable Long id, @AuthenticationPrincipal String memEmail) {
        ResponseDto responseDto = bookReportService.likePost(id, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @DeleteMapping("/board/report/{id}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long id) {
        ResponseDto responseDto = bookReportService.deletePost(id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 댓글 작성
    @PostMapping("/board/report/{id}")
    public ResponseEntity<ResponseDto> writeReply(@AuthenticationPrincipal String memEmail,
                                                  @PathVariable Long id, @RequestBody BookReportReplyDto.Request requestDto) {
        ResponseDto responseDto = bookReportReplyService.writeReply(memEmail, id, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping("/board/report/{id}/{replyId}")
    public ResponseEntity<ResponseDto> updateReply(@PathVariable Long id, @PathVariable Long replyId,
                                                   @RequestBody BookReportReplyUpdateDto requestDto) {

        ResponseDto responseDto = bookReportReplyService.updateReply(id, replyId, requestDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @DeleteMapping("/board/report/{id}/{replyId}")
    public ResponseEntity<ResponseDto> deleteReply(@PathVariable Long id, @PathVariable Long replyId) {
        ResponseDto responseDto = bookReportReplyService.deleteReply(id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/report/{id}/{replyId}/like")
    public ResponseEntity<ResponseDto> likeReply(@AuthenticationPrincipal String memEmail, @PathVariable Long id,
                                                 @PathVariable Long replyId) {

        ResponseDto responseDto = bookReportReplyService.likeReply(memEmail, id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
