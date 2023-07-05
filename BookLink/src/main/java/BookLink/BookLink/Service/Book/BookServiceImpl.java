package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.*;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

//    @Value("${kakao.key}")
//    private String key;
//    private String url = "https://dapi.kakao.com/v3/search/book";

    private final BookRepository bookRepository;
    private final BookRentRepository bookRentRepository;

    private String search_url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbelwlahstmxjf2304001&&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&InputEncoding=utf-8&Version=20131101";
    private String list_url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Bestseller&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
    private String key = "ttbelwlahstmxjf2057001";

    @Override
    public ResponseDto callApi(String query) { // findBook으로 바꾸고 callApi()로 공통 함수 만들기

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
//        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(search_url) //기본 url
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

    @Override
    public ResponseDto listAllBook(Integer category) { // 목록 조회

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(list_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        ResponseEntity<BookListDto> result_response = restTemplate.exchange(targetUrl, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = result_response.getBody();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("도서 목록 조회 완료");
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto searchBook(Integer category, String searchWord) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(search_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .queryParam("query", searchWord)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        ResponseEntity<BookListDto> result_response = restTemplate.exchange(targetUrl, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = result_response.getBody();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("검색 결과 조회 완료");
        responseDto.setData(result);

        return responseDto;
    }
}
