package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.Community.FreeBoardDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.BookClubService;
import BookLink.BookLink.Service.Community.BookReportService;
import BookLink.BookLink.Service.Community.FreeBoardService;
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
public class CommunityController {

    private final FreeBoardService freeBoardService;
    private final BookClubService bookClubService;
    private final BookReportService bookReportService;

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
                                       @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = freeBoardService.writePost(memEmail, freeBoardDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @PostMapping("/book-club") // 독서모임 글 작성
    public ResponseEntity<ResponseDto> writeBookClub (@RequestBody BookClubDto.Request bookClubDto,
                                      @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = bookClubService.writePost(memEmail, bookClubDto);

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
                                                    @AuthenticationPrincipal String memEmail) throws MalformedURLException {

        ResponseDto responseDto = bookClubService.showPost(memEmail, id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }


    @PostMapping("/board/report")
    public ResponseEntity<ResponseDto> writeReport(@RequestBody BookReportDto.Request requestDto,
                                                   @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = bookReportService.writeReport(requestDto, memEmail);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);

    }

    @GetMapping("/board/free")
    public ResponseEntity<ResponseDto> freeBoardList() {

        ResponseDto responseDto = freeBoardService.freeBoardList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/board/report")
    public ResponseEntity<ResponseDto> bookReportList() {

        ResponseDto responseDto = bookReportService.reportList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


}
