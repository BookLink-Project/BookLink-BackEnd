package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Review.ReviewDto;
import BookLink.BookLink.Service.Book.BookService;
import BookLink.BookLink.Service.Review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

    // 책 검색
    @GetMapping("/aladdin/search")
    public ResponseEntity<ResponseDto> callApi(@RequestParam String query) {

        ResponseDto responseDto = bookService.callApi(query);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 소장 도서 등록(일부정보만 일단 구현)
    // 임의로 BookSearchDto로 구현했습니다. 추후에 리펙토링할 예정.
    @PostMapping()
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
    public ResponseEntity<ResponseDto> showBook(@PathVariable String isbn) {

        ResponseDto responseDto = bookService.showBook(isbn);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}") // 후기 작성
    public ResponseEntity<ResponseDto> writeReview(@PathVariable String isbn,
                                                   @RequestBody ReviewDto reviewDto,
                                                   @AuthenticationPrincipal String memEmail) {

        log.info("member = {}", memEmail);
        ResponseDto responseDto = reviewService.writeReview(memEmail, isbn, reviewDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}