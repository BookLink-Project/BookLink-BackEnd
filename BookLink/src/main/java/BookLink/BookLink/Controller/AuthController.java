package BookLink.BookLink.Controller;

import BookLink.BookLink.OAuth.OAuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */

@RestController
@AllArgsConstructor
//@RequestMapping("/api/auth-test")
@RequestMapping("login/oauth2/code")
public class AuthController {

    private final OAuthService oAuthService;

    @PostMapping
    public ResponseEntity<String> authTest() { // with cookie input

        return ResponseEntity.ok().body("접근 성공");

    }

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
        String kakaoAccessToken = oAuthService.getKakaoAccessToken(code);
        oAuthService.createKakaoUser(kakaoAccessToken);
    }

}
