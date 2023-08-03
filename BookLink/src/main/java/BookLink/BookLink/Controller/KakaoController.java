package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.OAuth.KakaoMemberService;
import BookLink.BookLink.Domain.OAuth.SocialMemberInfoDto;
import BookLink.BookLink.Service.Member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;


/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    private final KakaoMemberService kakaoMemberService;

    // 카카오 로그인
    @GetMapping("/user/kakao/callback")
    public SocialMemberInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException, MalformedURLException {

        return kakaoMemberService.kakaoLogin(code, response);
        // 이메일, 패스워드를 뿌려주고 사용자가 패스워드를 인식하게한다.

    }


}