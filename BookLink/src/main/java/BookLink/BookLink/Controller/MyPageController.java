package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.MyPage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @PostMapping("/account") // 본인 인증
    public ResponseEntity<ResponseDto> verifyAccount(@RequestBody VerifyDto verifyDto,
                                                     @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = myPageService.verifyAccount(verifyDto, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/account") // 계정 정보
    public  ResponseEntity<ResponseDto> showAccount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = myPageService.showAccount(member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PatchMapping(value = "/account",
                  consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}) // 계정 정보 수정
    public ResponseEntity<ResponseDto> updateAccount(@RequestPart MultipartFile image,
                                                     @RequestPart AccountDto.Request data,
                                                     @AuthenticationPrincipal MemberPrincipal memberPrincipal)
                                                                                                throws IOException {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = myPageService.updateAccount(image, data, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("") // 나의 활동
    public ResponseEntity<ResponseDto> showMyHistory(@RequestParam(required = false, defaultValue = "payment") String rent,
                                                     @RequestParam(required = false, defaultValue = "review") String community,
                                                     @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = myPageService.showHistory(member, rent, community);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/my-book")
    public ResponseEntity<ResponseDto> myBook(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = myPageService.myBook(member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/my-book/rent/{page}")
    public ResponseEntity<ResponseDto> myRentBook(@PathVariable Integer page,
                                                  @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = myPageService.myRentBook(page, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @GetMapping("/my-book/lend/{page}")
    public ResponseEntity<ResponseDto> myLendBook(@PathVariable Integer page,
                                                  @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = myPageService.myLendBook(page, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 대여 신청 받지 않기
    @DeleteMapping("/my-book/rent/{book_id}")
    public ResponseEntity<ResponseDto> deleteRentBook(@PathVariable Long book_id,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = myPageService.blockRentBook(book_id, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


}
