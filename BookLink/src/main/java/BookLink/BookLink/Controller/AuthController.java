package BookLink.BookLink.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code")
public class AuthController {

//    private final OAuthService oAuthService;

    @PostMapping
    public ResponseEntity<String> authTest() { // with cookie input

        return ResponseEntity.ok().body("접근 성공");

    }

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
//    @GetMapping("/kakao")
//    public ResponseEntity<ResponseDto> kakaoCallback(HttpServletResponse response, @RequestParam String code) {
//        System.out.println(code);
//        String kakaoAccessToken = oAuthService.getKakaoAccessToken(response, code);
//        System.out.println("체크");
//        System.out.println(kakaoAccessToken);
//        ResponseDto responseDto = oAuthService.createKakaoUser(kakaoAccessToken);
//
//        return ResponseEntity.status(responseDto.getStatus())
//                .body(responseDto);
//    }

}
