package BookLink.BookLink.Controller.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardUpdateDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.FreeBoard.FreeBoardService;
import BookLink.BookLink.Service.CommunityReply.FreeBoard.FreeBoardReplyService;
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
                                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = freeBoardService.writePost(member, freeBoardDto);

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
    public ResponseEntity<ResponseDto> freeBoardDetail(@PathVariable Long id, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = freeBoardService.freeBoardDetail(id, member);

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
    public ResponseEntity<ResponseDto> likePost(@PathVariable Long id, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = freeBoardService.likePost(id, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/board/free/{id}")
    public ResponseEntity<ResponseDto> writeReply(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                                  @PathVariable Long id, @RequestBody FreeBoardReplyDto.Request requestDto) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = freeBoardReplyService.writeReply(member, id, requestDto);

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
    public ResponseEntity<ResponseDto> likeReply(@AuthenticationPrincipal MemberPrincipal memberPrincipal, @PathVariable Long id,
                                                 @PathVariable Long replyId) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = freeBoardReplyService.likeReply(member, id, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
