package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.EmailDto;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Service.MemberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<Void> joinMember(@RequestBody MemberDto.Request memberDTO) {
        memberService.joinMember(memberDTO);
        return ResponseEntity.ok().build(); // 상태코드만 반환해줄때 build() 필요
        /*
        return ResponseEntity.ok()
            .headers(headers)
            .body(moveResponseDto);
         */
    }

    @PostMapping(value = "/email/double-check")
    public ResponseEntity<Void> emailDoubleCheck(@RequestBody EmailDto emailDto) {
        boolean is_exist = memberService.emailDoubleCheck(emailDto.getEmail());

        if(is_exist) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok().build();
        }
    }

}
