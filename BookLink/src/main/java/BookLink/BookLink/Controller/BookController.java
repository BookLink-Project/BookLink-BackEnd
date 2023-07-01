package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Book.BookSearchDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

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





}
