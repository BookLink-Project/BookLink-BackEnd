package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.MyPage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/account") // 계정 정보 수정
    public ResponseEntity<ResponseDto> updateAccount(@RequestBody AccountDto.Request accountDto,
                                                     @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = myPageService.updateAccount(accountDto, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

//    @GetMapping("/my-book/rent/{page}")
//    public ResponseEntity<ResponseDto> myRentBook(@PathVariable Integer page) {
//
//
//    }

    @GetMapping("/my-book")
    public ResponseEntity<ResponseDto> myBook(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();

        ResponseDto responseDto = myPageService.myBook(member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

}
