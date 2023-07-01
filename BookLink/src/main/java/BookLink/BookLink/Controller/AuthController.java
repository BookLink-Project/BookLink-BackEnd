package BookLink.BookLink.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */

@RestController
@RequestMapping("/api/auth-test")
public class AuthController {

    @PostMapping
    public ResponseEntity<String> authTest() { // with header input

        return ResponseEntity.ok().body("접근 성공");

    }

}
