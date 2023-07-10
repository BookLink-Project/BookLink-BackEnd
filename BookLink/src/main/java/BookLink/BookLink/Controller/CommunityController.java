package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Community.FreeBoardDto;
import BookLink.BookLink.Domain.ResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/communities")
public class CommunityController {

    @GetMapping() // 커뮤니티 홈
    public ResponseDto communityHome() {
        return null;
    }

    @GetMapping(value = {"/board/{category}", "/board"}) // 게시판 리스트 (자유글 + 독후감)
    public ResponseDto listBoard(@PathVariable String category) {
        return null;
    }

    @PostMapping("/board/free") // 자유글 작성
    public ResponseDto writePost(@RequestBody FreeBoardDto freeBoardDto) {
        return null;
    }


}
