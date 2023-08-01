package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Email.EmailDto;
import BookLink.BookLink.Domain.Member.*;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Exception.Enum.CommonErrorCode;
import BookLink.BookLink.Exception.RestApiException;
import BookLink.BookLink.Service.Email.EmailService;
import BookLink.BookLink.Service.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailservice;

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDto> joinMember(@RequestBody MemberDto.Request memberDto) {
        ResponseDto responseDto = memberService.joinMember(memberDto);
        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto); // 상태코드만 반환해줄때 build() 필요
        /*
        return ResponseEntity.ok()
            .headers(headers)
            .body(moveResponseDto);
         */
    }

    @PostMapping(value = "/email/double-check")
    public ResponseEntity<ResponseDto> emailDoubleCheck(@RequestBody EmailDto.Request emailDto) {
        ResponseDto responseDto = memberService.emailDoubleCheck(emailDto.getEmail());

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping(value = "/nickname/double-check")
    public ResponseEntity<ResponseDto> emailDoubleCheck(@RequestBody NicknameDto nicknameDto) {
        ResponseDto responseDto = memberService.nicknameDoubleCheck(nicknameDto.getNickname());

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping(value = "/email/email-auth")
    public ResponseEntity<EmailDto.Response> emailSend(@RequestBody EmailDto.Request emailDto) throws Exception {

        EmailDto.Response responseDto = emailservice.sendSimpleMessage(emailDto.getEmail());

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @PostMapping(value = "/email/confirm")
    public ResponseEntity<ResponseDto> emailConfirm(@RequestBody EmailDto.Request emailDto) {
        ResponseDto responseDto = emailservice.confirmMessage(emailDto);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginMember(@RequestBody LoginDto.Request loginDto,
                                                HttpServletResponse response) throws Exception {

        ResponseDto responseDto = memberService.loginJwt(loginDto, response);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logoutMember(HttpServletResponse response,
                                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        ResponseDto responseDto = memberService.logoutJwt(response, memberPrincipal);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


//    @PostMapping("/error-test")
//    public ResponseEntity<Member> getTest() {
//    }
}