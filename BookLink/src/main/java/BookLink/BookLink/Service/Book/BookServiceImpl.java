package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.*;
import BookLink.BookLink.Domain.Common.RentStatus;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookRepliesDto;
import BookLink.BookLink.Exception.Enum.BookErrorCode;
import BookLink.BookLink.Exception.RestApiException;
import BookLink.BookLink.Repository.Book.*;
import BookLink.BookLink.Repository.BookReply.BookReplyLikeRepository;
import BookLink.BookLink.Repository.Community.BookReport.BookReportRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Service.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.Multipart;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

//    @Value("${kakao.key}")
//    private String key;
//    private String url = "https://dapi.kakao.com/v3/search/book";

    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final BookRentRepository bookRentRepository;
    private final BookReplyRepository bookReplyRepository;
    private final BookReplyLikeRepository bookReplyLikeRepository;
    private final BookReportRepository bookReportRepository;
    private final RentRepository rentRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final BookImageRepository bookImageRepository;

    private String search_url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Title&MaxResults=32&SearchTarget=Book&Cover=Big&output=js&InputEncoding=utf-8&Version=20131101";
    private String list_url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbelwlahstmxjf2304001&QueryType=Bestseller&MaxResults=32&SearchTarget=Book&Cover=Big&output=js&Version=20131101";
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
    public ResponseDto listAllBook(Integer category, int page) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUrl = UriComponentsBuilder
                .fromUriString(list_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .queryParam("outofStockfilter", 1)
                .queryParam("Start", page)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookListDto> resultResponse = restTemplate.exchange(targetUrl, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = resultResponse.getBody();

        if (result == null) {
            return responseDto;
        }
        List<BookListDto.Item> items = result.getItem()
                                        .stream()
                                        .filter(item -> !item.getIsbn13().isEmpty())
                                        .collect(Collectors.toList()); // isbn 필터링

        for (BookListDto.Item item : items) {

            String isbn = item.getIsbn13();

            Long like_cnt = bookLikeRepository.countByIsbn(isbn);
            Long reply_cnt = bookReplyRepository.countByIsbn(isbn);
            Long owner_cnt = bookRepository.countByIsbn(isbn);

            item.setLike_cnt(like_cnt);
            item.setReply_cnt(reply_cnt);
            item.setOwner_cnt(owner_cnt);
        }
        result.setItem(items);
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto searchBook(Integer category, String searchWord, int page) {

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        URI targetUri = UriComponentsBuilder
                .fromUriString(search_url)
                .queryParam("CategoryId", category != null ? category : 0)
                .queryParam("Query", searchWord)
                .queryParam("outofStockfilter", 1)
                .queryParam("Start", page)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookListDto> resultResponse = restTemplate.exchange(targetUri, HttpMethod.GET, null, BookListDto.class);

        BookListDto result = resultResponse.getBody();

        if (result == null) {
            return responseDto;
        }
        List<BookListDto.Item> items = result.getItem()
                                        .stream()
                                        .filter(item -> !item.getIsbn13().isEmpty())
                                        .collect(Collectors.toList()); // isbn 필터링

        for (BookListDto.Item item : items) {

            String isbn = item.getIsbn13();

            Long like_cnt = bookLikeRepository.countByIsbn(isbn);
            Long reply_cnt = bookReplyRepository.countByIsbn(isbn);
            Long owner_cnt = bookRepository.countByIsbn(isbn);

            item.setCategoryName(item.getCategoryName().split(">")[1]);

            item.setLike_cnt(like_cnt);
            item.setReply_cnt(reply_cnt);
            item.setOwner_cnt(owner_cnt);
        }
        result.setItem(items);
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    public ResponseDto showBook(MemberPrincipal memberPrincipal, String isbn13) {

        Member loginMember = (memberPrincipal == null) ? null : memberPrincipal.getMember();

        ResponseDto responseDto = new ResponseDto();

        RestTemplate restTemplate = new RestTemplate();

        BookDetailDto result = showBookApi(isbn13, restTemplate);

        if (result == null) {
            return responseDto; // all null
        }
        if (result.getItem().isEmpty()) {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setMessage("없는 책");
            return responseDto;
        }

        // 1. 책 정보
        BookDetailDto.Item item = result.getItem().get(0);

        String isbn = item.getIsbn13();
        Long like_cnt = bookLikeRepository.countByIsbn(isbn);
        Long reply_cnt = bookReplyRepository.countByIsbn(isbn);
        Long owner_cnt = bookRepository.countByIsbn(isbn);

        boolean isLikedBook = (loginMember != null)
                && (bookLikeRepository.existsByMemberAndIsbn(loginMember, isbn));

        item.setCategoryName(item.getCategoryName().split(">")[1]);

        item.setLike_cnt(like_cnt);
        item.setReply_cnt(reply_cnt);
        item.setOwner_cnt(owner_cnt);
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

            boolean isLikedReply = (loginMember != null)
                    && (bookReplyLikeRepository.existsByMemberAndReply(loginMember, reply));

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
                    .id(report.getId())
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

    public BookDetailDto showBookApi(String isbn13, RestTemplate restTemplate) {

        URI bookUri = UriComponentsBuilder
                .fromUriString(detail_url)
                .queryParam("ItemId", isbn13)
                .build().encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<BookDetailDto> resultResponse = restTemplate.exchange(bookUri, HttpMethod.GET, null, BookDetailDto.class);

        return resultResponse.getBody(); // not return null
    }

    @Override
    public ResponseDto likeBook(Member loginMember, String isbn) {

        ResponseDto responseDto = new ResponseDto();

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
    public ResponseDto joinMyBook(BookDto.Request bookDto, Member loginMember, List<MultipartFile> image) throws IOException {

        ResponseDto responseDto = new ResponseDto();
        List<BookImage> urlList = new ArrayList<>();

        boolean is_exist = bookRepository.existsByIsbnAndWriter(bookDto.getIsbn13(), loginMember);

        if(is_exist) {
            throw new RestApiException(BookErrorCode.ALREADY_SAVED_BOOK);
        }

        if(bookDto.getRent_signal()) {

            BookRent bookRent = new BookRent();

            bookRent = BookRent.builder()
                    .rent_status(RentStatus.WAITING)
                    .book_rating(bookDto.getBook_rating())
                    .book_status(bookDto.getBook_status())
                    .rental_fee(bookDto.getRental_fee())
                    .min_date(bookDto.getMin_date())
                    .max_date(bookDto.getMax_date())
                    .rent_location(bookDto.getRent_location())
                    .rent_method(bookDto.getRent_method())
                    .build();

            bookRentRepository.save(bookRent);

            for (MultipartFile multipartFile : image) {
                URL imageUrl = s3Service.uploadImage(multipartFile);

                BookImage bookImage = new BookImage(imageUrl, bookRent);
                bookImageRepository.save(bookImage);
                urlList.add(bookImage);
            }

            Book book = BookDto.Request.toBookEntity(bookDto, bookRent, loginMember);
            bookRepository.save(book);
        }
        else{
            Book book = BookDto.Request.toBookEntity(bookDto, loginMember);
            bookRepository.save(book);
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("DB 저장 완료");
        return responseDto;
    }

    private List<BookRentDto> processChunkTitles(List<String> titles, Integer page) {

        int chunkSize = 16;
        int totalSize = titles.size();

        if (totalSize < chunkSize) {
            chunkSize = totalSize;
        }

        List<String> chunkDistinctTitles = titles.subList((page) * chunkSize, (page + 1) * chunkSize);

        List<BookRentDto> chunkBookRentList = new ArrayList<>();

        for (String title : chunkDistinctTitles) {

            int avg_fee = 0;
            int total_fee = 0;
            int count = 0;
            int max_period = 0;

            List<Book> books = bookRepository.findByTitle(title);
            count = books.size();

            for (Book book : books) {
                BookRent bookRent = book.getBookRent();
                Integer rental_fee = bookRent.getRental_fee();
                Integer max_date = bookRent.getMax_date();

                if (max_date > max_period) {
                    max_period = max_date;
                }

                total_fee += rental_fee;
            }

            avg_fee = total_fee / count;

            Book book = books.get(0);

            BookRentDto bookRentListDto = BookRentDto.builder()
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .isbn(book.getIsbn())
                    .cover(book.getCover())
                    .publisher(book.getPublisher())
                    .avg_rental_fee(avg_fee)
                    .rent_period(max_period)
                    .build();

            chunkBookRentList.add(bookRentListDto);
        }

        return chunkBookRentList;
    }

    @Override
    public ResponseDto rentBookList(Integer page) {

        ResponseDto responseDto = new ResponseDto();

        List<String> titles = bookRepository.findDistinctTitles();

        List<BookRentDto> bookRentDtoList = processChunkTitles(titles, page);

        responseDto.setData(bookRentDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto rentBookDescList(Integer page) {

        ResponseDto responseDto = new ResponseDto();

        List<String> titles = bookRepository.findTitlesOrderByTitleCountDesc();

        List<BookRentDto> bookRentDtoList = processChunkTitles(titles, page);

        responseDto.setData(bookRentDtoList);
        return responseDto;

    }

    @Override
    public ResponseDto rentBookCategoryList(String category, Integer page) {

        ResponseDto responseDto = new ResponseDto();

        List<String> titles = bookRepository.findTitlesByCategory_name(category);

        List<BookRentDto> bookRentDtoList = processChunkTitles(titles, page);

        responseDto.setData(bookRentDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto rentBookCategoryDescList(String category, Integer page) {

        ResponseDto responseDto = new ResponseDto();

        List<String> titles = bookRepository.findTitlesByCategory_nameCountDesc(category);

        List<BookRentDto> bookRentDtoList = processChunkTitles(titles, page);

        responseDto.setData(bookRentDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto rentBookSearch(String title) {

        ResponseDto responseDto = new ResponseDto();

        int avg_fee = 0;
        int total_fee = 0;
        int count = 0;
        int max_period = 0;

        List<Book> books = bookRepository.findByTitleContaining(title);
        count = books.size();

        for (Book book : books) {
            BookRent bookRent = book.getBookRent();
            Integer rental_fee = bookRent.getRental_fee();
            Integer max_date = bookRent.getMax_date();

            if (max_date > max_period) {
                max_period = max_date;
            }

            total_fee += rental_fee;
        }

        avg_fee = total_fee / count;

        Book book = books.get(0);

        BookRentDto bookRentDto = BookRentDto.builder()
                .title(book.getTitle())
                .authors(book.getAuthors())
                .isbn(book.getIsbn())
                .cover(book.getCover())
                .publisher(book.getPublisher())
                .avg_rental_fee(avg_fee)
                .rent_period(max_period)
                .build();

        responseDto.setData(bookRentDto);

        return responseDto;
    }

    @Override
    public ResponseDto rentBooks(String title) {

        ResponseDto responseDto = new ResponseDto();
        List<BookRentInfoDto> bookRentInfoDtoList = new ArrayList<>();

        List<Book> books = bookRepository.findByTitle(title);

        for (Book book : books) {

            BookRent bookRent = book.getBookRent();

            BookRentInfoDto bookRentInfoDto = BookRentInfoDto.builder()
                    .isbn(book.getIsbn())
                    .writer(book.getWriter().getNickname())
                    .created_time(bookRent.getCreatedTime())
                    .book_rating(bookRent.getBook_rating())
                    .rental_fee(bookRent.getRental_fee())
                    .max_date(bookRent.getMax_date())
                    .rent_location(bookRent.getRent_location())
                    .build();

            bookRentInfoDtoList.add(bookRentInfoDto);
        }

        responseDto.setData(bookRentInfoDtoList);

        return responseDto;
    }

    @Override
    public ResponseDto rentBookDetail(Long id) {

        ResponseDto responseDto = new ResponseDto();

        List<BookRecordDto> bookRecordDtoList = new ArrayList<>();
        List<BookRentInfoDto> bookRentInfoDtoList = new ArrayList<>();

        int rent_available_cnt = 0;
        int renting_cnt = 0;

        Book book_byId = bookRepository.findById(id).orElse(null);

        if (book_byId == null) {
            responseDto.setMessage("기록되지 않은 책입니다.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            return responseDto;
        }

        BookRent bookRent_byId = book_byId.getBookRent();

        Member member = book_byId.getWriter();
        List<Book> books = member.getBooks(); // 해당 회원이 기록한 책들

        for (Book book : books) {
            BookRent bookRent = book.getBookRent();

            RentStatus rent_status = bookRent.getRent_status();

            if (rent_status == RentStatus.RENTING) {
                renting_cnt += 1;
            } else {
                rent_available_cnt += 1;
            }

            BookRecordDto bookRecordDto = BookRecordDto.builder()
                    .rent_status(rent_status)
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .isbn(book.getIsbn())
                    .cover(book.getCover())
                    .created_time(book.getCreatedTime())
                    .publisher(book.getPublisher())
                    .rental_fee(bookRent.getRental_fee())
                    .build();

            bookRecordDtoList.add(bookRecordDto);
        }

        List<Book> another_books = bookRepository.findByTitle(book_byId.getTitle());

        for (Book another_book : another_books) {
            BookRent bookRent = another_book.getBookRent();

            BookRentInfoDto bookRentInfoDto = BookRentInfoDto.builder()
                    .writer(another_book.getWriter().getNickname())
                    .created_time(bookRent.getCreatedTime())
                    .book_rating(bookRent.getBook_rating())
                    .rental_fee(bookRent.getRental_fee())
                    .max_date(bookRent.getMax_date())
                    .rent_location(bookRent.getRent_location())
                    .build();

            bookRentInfoDtoList.add(bookRentInfoDto);
        }

        BookRentDetailDto bookRentDetailDto = BookRentDetailDto.builder()
                .record_cnt(books.size())
                .rent_available_cnt(rent_available_cnt)
                .renting_cnt(renting_cnt)
                .bookRecordDtoList(bookRecordDtoList)
                .title(book_byId.getTitle())
                .authors(book_byId.getAuthors())
                .recommendation(book_byId.getRecommendation())
                .isbn(book_byId.getIsbn())
                .cover(book_byId.getCover())
                .publisher(book_byId.getPublisher())
                .book_rating(bookRent_byId.getBook_rating())
                .rent_location(bookRent_byId.getRent_location())
                .rent_method(bookRent_byId.getRent_method())
                .min_date(bookRent_byId.getMin_date())
                .max_date(bookRent_byId.getMax_date())
                .rental_fee(bookRent_byId.getRental_fee())
                .book_status(bookRent_byId.getBook_status())
                .bookRentInfoDtoList(bookRentInfoDtoList)
                .build();

        responseDto.setData(bookRentDetailDto);
        return responseDto;

    }

    @Override
    public ResponseDto rentSuccess(Long id, RentDto rentDto, Member lender) {

        ResponseDto responseDto = new ResponseDto();

        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            responseDto.setMessage("없는 책 입니다.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            return responseDto;
        }

        Member renter = memberRepository.findByNickname(rentDto.getRent_nickname()).orElse(null);

        if (lender == null) {
            responseDto.setMessage("없는 회원입니다.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            return responseDto;
        }

        Rent rent = Rent.builder()
                .book(book)
                .lender(lender)
                .renter(renter)
                .rent_date(rentDto.getRent_date())
                .return_date(rentDto.getReturn_date())
                .return_location(rentDto.getReturn_location())
                .build();

        rentRepository.save(rent);

        return responseDto;
    }
}




