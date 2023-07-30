package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.*;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookRepliesDto;
import BookLink.BookLink.Exception.Enum.BookErrorCode;
import BookLink.BookLink.Exception.RestApiException;
import BookLink.BookLink.Repository.Book.BookLikeRepository;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyLikeRepository;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    private final BookReportRepository bookReportRepository;

    private String search_url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Title&MaxResults=30&start=1&SearchTarget=Book&Cover=Big&output=js&InputEncoding=utf-8&Version=20131101";
    private String list_url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Bestseller&MaxResults=30&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101";
    private String detail_url = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbelwlahstmxjf2304001&itemIdType=ISBN13&Cover=Big&output=js&Version=20131101";
    private String key = "ttbelwlahstmxjf2057001";

    @Override
    public ResponseDto callApi(String query) { // TODO findBook으로 바꾸고 callApi()로 공통 함수 만들기

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
            item.setOwner_cnt(0L); // TODO dummy
        }

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
            item.setOwner_cnt(0L); // TODO dummy
        }

        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto showBook(String memEmail, String isbn13) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI bookUri = UriComponentsBuilder
                .fromUriString(detail_url)
                .queryParam("ItemId", isbn13)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookDetailDto> resultResponse = restTemplate.exchange(bookUri, HttpMethod.GET, null, BookDetailDto.class);

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

        // 1. 책 정보
        BookDetailDto.Item item = result.getItem().get(0);

        String isbn = item.getIsbn13();
        Long like_cnt = bookLikeRepository.countByIsbn(isbn); // 좋아요 수
        Long reply_cnt = bookReplyRepository.countByIsbn(isbn); // 댓글 수

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);
        boolean isLikedBook = bookLikeRepository.existsByMemberAndIsbn(loginMember, isbn); // 좋아요 상태

        item.setCategoryName(item.getCategoryName().split(">")[1]);

        item.setLike_cnt(like_cnt);
        item.setReply_cnt(reply_cnt);
        item.setOwner_cnt(0L); // TODO dummy
        item.setLiked(isLikedBook);

        // 2. 댓글 정보
        List<BookReply> replyList = bookReplyRepository.findByIsbnOrderByParentDescIdDesc(isbn13);

        List<BookRepliesDto> replies = new ArrayList<BookRepliesDto>();

        for (BookReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            // 대댓글 수 (부모 : 자식)
            Long sub_reply_cnt = parentId.equals(replyId) ? bookReplyRepository.countByParentId(parentId) - 1 : 0;

            boolean isLikedReply = bookReplyLikeRepository.existsByMemberAndReply(loginMember, reply); // 좋아요 상태

            BookRepliesDto rv;

            rv = new BookRepliesDto(
                    replyId,
                    parentId,
                    writer.getNickname(),
                    reply.getContent(),
                    reply.getCreatedTime(),
                    writer.getImage(),
                    reply.getLike_cnt(),
                    sub_reply_cnt,
                    isLikedReply,
                    reply.isUpdated()
            );
            replies.add(rv);
        }
        result.setReplies(replies);

        // 3. 카테고리 추천 도서
        int categoryId = item.getCategoryId();

        URI recommendUri = UriComponentsBuilder
                .fromUriString(list_url)
                .queryParam("CategoryId", categoryId)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookRecommendDto> rec_resultResponse = restTemplate.exchange(recommendUri, HttpMethod.GET, null, BookRecommendDto.class);

        BookRecommendDto rec_result = rec_resultResponse.getBody();

        List<BookRecommendDto.Item> recommend_books = new ArrayList<>();

        if (rec_result != null) {

            List<Integer> nums = new ArrayList<>();

            int num;
            for (int i = 0; i < 3; i++) {
                while (true) { // 중복 제거
                    num = (int) (Math.random() * 30);
                    log.info("num = {}", num);
                    if (!nums.contains(num)) {
                        nums.add(num);
                        break;
                    }
                }
                BookRecommendDto.Item item1 = rec_result.getItem().get(num);
                recommend_books.add(item1);
            }
        }
        result.setRecommended_books(recommend_books);

        // 4. 관련 게시글
        List<BookRelatedPostDto> related_posts = new ArrayList<>();

        List<BookReport> reports = bookReportRepository.findTop5ByIsbnOrderByCreatedTimeDesc(isbn);

        for (BookReport report : reports) {
            BookRelatedPostDto post = BookRelatedPostDto.builder()
                    .title(report.getTitle())
                    .content(report.getContent())
                    .date(report.getCreatedTime())
                    .writer(report.getWriter().getNickname())
                    .image(report.getWriter().getImage())
                    .build();
            related_posts.add(post);
        }
        result.setRelated_posts(related_posts);

        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto likeBook(String memEmail, String isbn) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookLike bookLike = bookLikeRepository.findByMemberAndIsbn(loginMember, isbn).orElse(null);

        if (bookLike == null) { // 좋아요 안 눌린 상태

            bookLike = BookLike.builder()
                    .isbn(isbn)
                    .member(loginMember)
                    .build();

            bookLikeRepository.save(bookLike);

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            bookLikeRepository.delete(bookLike);

            responseDto.setMessage("좋아요 취소 성공");

        }

        BookLikeDto bookLikeDto = new BookLikeDto(bookLikeRepository.countByIsbn(isbn));
        responseDto.setData(bookLikeDto);

        return responseDto;

    }


    @Override
    public ResponseDto joinMyBook(BookDto.Request bookDto) {

        boolean is_exist = bookRepository.existsByIsbn(bookDto.getIsbn13());

        if(is_exist) {
            throw new RestApiException(BookErrorCode.ALREADY_SAVED_BOOK);
        }

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
}
