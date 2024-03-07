package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HomeController {

    private final BookService bookService;

    @GetMapping("")
    public ResponseEntity<ResponseDto> searchBook(@RequestParam(required = false) String search,
                                                  @RequestParam(required = false, defaultValue = "1") Integer page) {

        ResponseDto responseDto = bookService.searchHeaderBook(search, page);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
