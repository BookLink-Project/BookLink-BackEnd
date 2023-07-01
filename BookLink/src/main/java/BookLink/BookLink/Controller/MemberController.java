package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Email.EmailDto;
import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.EmailService.Emailservice;
import BookLink.BookLink.Service.MemberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final Emailservice emailservice;

    @PostMapping()
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

    @PostMapping(value = "/double-check/email")
    public ResponseEntity<ResponseDto> emailDoubleCheck(@RequestBody EmailDto.Request emailDto) {
        ResponseDto responseDto = memberService.emailDoubleCheck(emailDto.getEmail());

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping(value = "/double-check/{nickname}")
    public ResponseEntity<ResponseDto> emailDoubleCheck(@PathVariable String nickname) {
        ResponseDto responseDto = memberService.nicknameDoubleCheck(nickname);

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<EmailDto.Response> emailConfirm(@RequestBody EmailDto.Request emailDto) throws Exception {

        EmailDto.Response responseDto = emailservice.sendSimpleMessage(emailDto.getEmail());

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @PostMapping("/login") // TODO refactoring without response
    public ResponseEntity<ResponseDto> loginMember(@RequestBody LoginDto.Request loginDto,
                                                HttpServletResponse response) throws Exception {

        System.out.println("MemberController.loginJwt");

        ResponseDto responseDto = memberService.loginJwt(loginDto, response);

//        if(responseDto.getStatus() == HttpStatus.OK) {
//            return ResponseEntity.ok()
//                    .body(responseDto);
//        }
//        else {
//            return ResponseEntity.badRequest()
//                    .body(responseDto);
//        }

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}