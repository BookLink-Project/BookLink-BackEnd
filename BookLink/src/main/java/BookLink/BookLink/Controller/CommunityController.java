package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.Community.FreeBoardDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Community.BookClubService;
import BookLink.BookLink.Service.Community.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities")
public class CommunityController {

    private final FreeBoardService freeBoardService;
    private final BookClubService bookClubService;

    @GetMapping() // 커뮤니티 홈
    public ResponseDto communityHome() {
        return null;
    }

    @GetMapping(value = {"/board/{category}", "/board"}) // 게시판 리스트 (자유글 + 독후감)
    public ResponseDto listBoard(@PathVariable String category) {
        return null;
    }

    @PostMapping("/board/free") // 자유글 작성
    public ResponseDto writeFreeBoard (@RequestBody FreeBoardDto freeBoardDto,
                                       @AuthenticationPrincipal String memEmail) {

        return freeBoardService.writePost(memEmail, freeBoardDto);

    }

    @PostMapping("/book-club") // 독서모임 글 작성
    public ResponseDto writeBookClub (@RequestBody BookClubDto bookClubDto,
                                      @AuthenticationPrincipal String memEmail) {

        return bookClubService.writePost(memEmail, bookClubDto);

    }


}
