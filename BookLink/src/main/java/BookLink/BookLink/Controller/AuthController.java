package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.AuthorizationDto;
import BookLink.BookLink.Service.OAuth.OAuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/auth-test")
@RequestMapping("/login/oauth2/code")
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
    @PostMapping("/kakao")
    public ResponseEntity<ResponseDto> kakaoCallback(HttpServletResponse response, @RequestBody AuthorizationDto authorizationDto) {
        System.out.println(authorizationDto.getCode());
        String kakaoAccessToken = oAuthService.getKakaoAccessToken(response, authorizationDto.getCode());
        System.out.println("체크");
        System.out.println(kakaoAccessToken);
        ResponseDto responseDto = oAuthService.createKakaoUser(kakaoAccessToken);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

}
