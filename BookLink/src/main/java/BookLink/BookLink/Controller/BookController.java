package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;
import BookLink.BookLink.Service.Book.BookService;
import BookLink.BookLink.Service.BookReply.BookReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookReplyService bookReplyService;

    // 책 검색
    @GetMapping("/aladdin/search")
    public ResponseEntity<ResponseDto> callApi(@RequestParam String query) {

        ResponseDto responseDto = bookService.callApi(query);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerMyBook(@RequestBody BookDto.Request bookDto) {

        ResponseDto responseDto = bookService.joinMyBook(bookDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping(value = {"/main/{category}", "/main"}) // 카테고리 분류 및 검색
    public ResponseEntity<ResponseDto> searchAndListBook(@PathVariable(required = false) Integer category,
                                                @RequestParam(required = false) String search) {

        // TODO refactoring

        ResponseDto responseDto;

        if (search == null) { // 분류
            responseDto = bookService.listAllBook(category);
        } else { // 검색
            responseDto = bookService.searchBook(category, search);
        }

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/{isbn}") // 상세 조회
    public ResponseEntity<ResponseDto> showDetail(@PathVariable String isbn,
                                                  @AuthenticationPrincipal String memEmail) throws MalformedURLException {

        ResponseDto responseDto = bookService.showBook(memEmail, isbn);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}") // 후기(댓글) 작성
    public ResponseEntity<ResponseDto> writeReply(@PathVariable String isbn,
                                                  @RequestBody BookReplyDto.Request replyDto,
                                                  @AuthenticationPrincipal String memEmail) throws MalformedURLException {

        ResponseDto responseDto = bookReplyService.writeReply(memEmail, isbn, replyDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}/like/{state}") // 도서 좋아요 클릭
    public ResponseEntity<ResponseDto> clickBookLike (@PathVariable String isbn,
                                                      @PathVariable String state,
                                                      @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = bookService.likeBook(memEmail, isbn, state);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}/{replyId}/like/{state}") // 후기 좋아요 클릭
    public ResponseEntity<ResponseDto> clickReplyLike (@PathVariable String isbn,
                                                      @PathVariable Long replyId,
                                                      @PathVariable String state,
                                                      @AuthenticationPrincipal String memEmail) {

        ResponseDto responseDto = bookReplyService.likeReply(memEmail, replyId, state);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


}