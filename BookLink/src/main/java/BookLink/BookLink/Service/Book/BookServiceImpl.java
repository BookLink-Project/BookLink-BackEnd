package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookRepliesDto;
import BookLink.BookLink.Repository.Book.BookLikeRepository;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyLikeRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

//    @Value("${kakao.key}")
//    private String key;
//    private String url = "https://dapi.kakao.com/v3/search/book";

    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final BookRentRepository bookRentRepository;
    private final BookReplyRepository bookReplyRepository;
    private final BookReplyLikeRepository bookReplyLikeRepository;
    private final MemberRepository memberRepository;

    private String search_url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&Cover=Big&output=js&InputEncoding=utf-8&Version=20131101";
    private String list_url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Bestseller&MaxResults=10&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101";
    private String detail_url = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbelwlahstmxjf2304001&itemIdType=ISBN13&Cover=Big&output=js&Version=20131101";
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
    public ResponseDto listAllBook(Integer category) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(list_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        ResponseEntity<BookListDto> resultResponse = restTemplate.exchange(targetUrl, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = resultResponse.getBody();

        if (result == null) {
            return responseDto;
        }

        List<BookListDto.Item> items = result.getItem();

        for (BookListDto.Item item : items) {
            String isbn = item.getIsbn13();
            Long like_cnt = bookLikeRepository.countByIsbn(isbn); // 좋아요 수
            Long reply_cnt = bookReplyRepository.countByIsbn(isbn); // 댓글 수

            item.setLike_cnt(like_cnt);
            item.setReply_cnt(reply_cnt);
            item.setOwner_cnt((long)(Math.random()*10)); // TODO dummy
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto searchBook(Integer category, String searchWord) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUri = UriComponentsBuilder
                .fromUriString(search_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .queryParam("query", searchWord)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookListDto> resultResponse = restTemplate.exchange(targetUri, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = resultResponse.getBody();

        if (result == null) {
            return responseDto;
        }

        List<BookListDto.Item> items = result.getItem();

        for (BookListDto.Item item : items) {
            String isbn = item.getIsbn13();
            Long like_cnt = bookLikeRepository.countByIsbn(isbn); // 좋아요 수
            Long reply_cnt = bookReplyRepository.countByIsbn(isbn); // 댓글 수

            item.setLike_cnt(like_cnt);
            item.setReply_cnt(reply_cnt);
            item.setOwner_cnt((long)(Math.random() * 10)); // TODO dummy
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto showBook(String memEmail, String isbn13) throws MalformedURLException {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUri = UriComponentsBuilder
                .fromUriString(detail_url)
                .queryParam("ItemId", isbn13)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookDetailDto> resultResponse = restTemplate.exchange(targetUri, HttpMethod.GET, null, BookDetailDto.class);

        BookDetailDto result = resultResponse.getBody(); // not return null

        if (result == null) {
            return responseDto; // all null
        }

        if (result.getItem().isEmpty()) {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setMessage("없는 책");
            responseDto.setData(null);

            return responseDto;
        }

        BookDetailDto.Item item = result.getItem().get(0);

        String isbn = item.getIsbn13();
        Long like_cnt = bookLikeRepository.countByIsbn(isbn); // 좋아요 수
        Long reply_cnt = bookReplyRepository.countByIsbn(isbn); // 댓글 수

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);
        boolean isLikedBook = bookLikeRepository.existsByMemberAndIsbn(loginMember, isbn);// 좋아요 상태

        item.setLike_cnt(like_cnt);
        item.setReply_cnt(reply_cnt);
        item.setOwner_cnt((long)(Math.random() * 10)); // TODO dummy
        item.setLiked(isLikedBook);

        // 댓글 조회
        List<BookReply> replyList = bookReplyRepository.findByIsbnOrderByParentAscIdAsc(isbn13); // TODO sorting

        List<BookRepliesDto> replies = new ArrayList<BookRepliesDto>();

        for (BookReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            // 부모 댓글의 경우 : 자식 댓글의 경우
            Long sub_reply_cnt = parentId.equals(replyId) ? bookReplyRepository.countByParentId(parentId) - 1 : 0; // 대댓글 수

            URL writerPic = new URL("https://m.blog.naver.com/yunam69/221690011454"); // TODO dummy

            boolean isLikedReply = bookReplyLikeRepository.existsByMemberAndReply(loginMember, reply);// 좋아요 상태

            BookRepliesDto rv = new BookRepliesDto(
                    replyId,
                    parentId,
                    writer.getNickname(),
                    reply.getContent(),
                    reply.getCreatedTime(),
                    writerPic,
                    reply.getLike_cnt(),
                    sub_reply_cnt,
                    isLikedReply
            );

            replies.add(rv);
        }

        result.setReplies(replies);

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto likeBook(String memEmail, String isbn, String state) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        if (state.equals("up")) {

            BookLike bookLike = BookLike.builder()
                    .isbn(isbn)
                    .member(loginMember)
                    .build();

            // TODO 혹시나 하는 예외 처리 (프론트에서 잘못 요청될 경우) with replyLike

            bookLikeRepository.save(bookLike);

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 성공");

        } else { // "down"

            // 테이블에 없는데 down 경우 -> IllegalArgumentException

            BookLike bookLike = bookLikeRepository.findByIsbnAndMember(isbn, loginMember);
            bookLikeRepository.delete(bookLike);

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 취소 성공");

        }

        return responseDto;
    }
}
