package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Book.BookSearchDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

//    @Value("${kakao.key}")
//    private String key;
//    private String url = "https://dapi.kakao.com/v3/search/book";

    private final BookRepository bookRepository;
    private final BookRentRepository bookRentRepository;

    private String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbelwlahstmxjf2304001&&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&InputEncoding=utf-8&Version=20131101";
    private String key = "ttbelwlahstmxjf2057001";

    @Override
    public ResponseDto callApi(String query) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
//        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", query) //인자
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        ResponseEntity<BookSearchDto> result = restTemplate.exchange(targetUrl, HttpMethod.GET,null, BookSearchDto.class);
        BookSearchDto body = result.getBody();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setData(body);

        return responseDto;
    }

    @Override
    public ResponseDto joinMyBook(BookDto.Request bookDto) {

        ResponseDto responseDto = new ResponseDto();

        if(bookDto.getRent_signal()) {
            BookRent bookRent = BookDto.Request.toRentEntity(bookDto);
            bookRentRepository.save(bookRent);

            Book book = Book.builder()
                    .title(bookDto.getTitle())
                    .authors(bookDto.getAuthor())
                    .description(bookDto.getDescription())
                    .isbn(bookDto.getIsbn13())
                    .price_sales(bookDto.getPrice_sales())
                    .price_standard(bookDto.getPrice_standard())
                    .cover(bookDto.getCover())
                    .category_name(bookDto.getCategory_name())
                    .publisher(bookDto.getPublisher())
                    .pud_date(bookDto.getPud_date())
                    .rent_signal(bookDto.getRent_signal())
                    .bookRent(bookRent)
                    .build();

            bookRepository.save(book);
        }
        else{
            Book book = BookDto.Request.toBookEntity(bookDto);
            bookRepository.save(book);
        }


        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("DB 저장 완료");
        return responseDto;

    }




}
