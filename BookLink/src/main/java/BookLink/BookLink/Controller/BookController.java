package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.BookReply.BookReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;
import BookLink.BookLink.Service.Book.BookService;
import BookLink.BookLink.Service.BookReply.BookReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookReplyService bookReplyService;

    @GetMapping("/aladdin/search")
    public ResponseEntity<ResponseDto> callApi(@RequestParam String query) { // 책 검색

        ResponseDto responseDto = bookService.callApi(query);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 소장도서 등록 및 대여 등록
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerMyBook(@RequestBody BookDto.Request bookDto,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = bookService.joinMyBook(bookDto, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping(value = {"/main/{category}", "/main"}) // 카테고리 분류 및 검색
    public ResponseEntity<ResponseDto> searchAndListBook(@PathVariable(required = false) Integer category,
                                                         @RequestParam(required = false) String search,
                                                         @RequestParam(required = false, defaultValue = "1") Integer page) {

        ResponseDto responseDto;

        if (search == null) { // 분류
            responseDto = bookService.listAllBook(category, page);
        } else { // 검색
            responseDto = bookService.searchBook(category, search, page);
        }

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/{isbn}") // 상세 조회
    public ResponseEntity<ResponseDto> showDetail(@PathVariable String isbn,
                                                  @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        ResponseDto responseDto = bookService.showBook(memberPrincipal, isbn);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}") // 후기(댓글) 작성
    public ResponseEntity<ResponseDto> writeReply(@PathVariable String isbn,
                                                  @RequestBody BookReplyDto.Request replyDto,
                                                  @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookReplyService.writeReply(member, isbn, replyDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping("/{isbn}/{replyId}") // 후기(댓글) 수정
    public ResponseEntity<ResponseDto> updateReply(@PathVariable String isbn,
                                                   @PathVariable Long replyId,
                                                   @RequestBody BookReplyUpdateDto replyDto) {

        ResponseDto responseDto = bookReplyService.updateReply(isbn, replyId, replyDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @DeleteMapping("/{isbn}/{replyId}") // 후기(댓글) 삭제
    public ResponseEntity<ResponseDto> deleteReply(@PathVariable String isbn,
                                                   @PathVariable Long replyId) {

        ResponseDto responseDto = bookReplyService.deleteReply(isbn, replyId);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}/like") // 도서 좋아요 클릭
    public ResponseEntity<ResponseDto> clickBookLike (@PathVariable String isbn,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookService.likeBook(member, isbn);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/{isbn}/{replyId}/like") // 후기 좋아요 클릭
    public ResponseEntity<ResponseDto> clickReplyLike (@PathVariable String isbn,
                                                       @PathVariable Long replyId,
                                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = bookReplyService.likeReply(member, replyId, isbn);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 가능 책 리스트뷰(업데이트순 - 디폴트)
    @GetMapping("/rent")
    public ResponseEntity<ResponseDto> rentBookList() {

        ResponseDto responseDto = bookService.rentBookList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 가능 책 리스트뷰(책 등록 건수 순서)
    @GetMapping("/rent/desc")
    public ResponseEntity<ResponseDto> rentBookDescList() {

        ResponseDto responseDto = bookService.rentBookDescList();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 책 카테고리
    @GetMapping("/rent/{category}")
    public ResponseEntity<ResponseDto> rentBookCategoryList(@PathVariable String category) {

        ResponseDto responseDto = bookService.rentBookCategoryList(category);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 책 카테고리(책 등록 건수 순서)
    @GetMapping("/rent/desc/{category}")
    public ResponseEntity<ResponseDto> rentBookCategoryDescList(@PathVariable String category) {

        ResponseDto responseDto = bookService.rentBookCategoryDescList(category);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 책 제목 검색
    @GetMapping("/rent/{title}")
    public ResponseEntity<ResponseDto> rentBookSearch(@PathVariable String title) {

        ResponseDto responseDto = bookService.rentBookSearch(title);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


}