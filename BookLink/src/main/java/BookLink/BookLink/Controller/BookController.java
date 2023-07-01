package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Service.Book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    // 책 검색
    @GetMapping("/aladin/book-search")
    public ResponseEntity<Map> callApi(@RequestParam String query) {

        Map response = bookService.callApi(query);

        return ResponseEntity.ok()
                .body(response);
    }





}
