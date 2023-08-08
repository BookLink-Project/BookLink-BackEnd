package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Service.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * Authorization 을 위한 접근 테스트 controller 입니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-test")
public class AuthTestController {

    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<String> authTest(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        return ResponseEntity.ok().body("접근 성공");

    }

    @PostMapping("/upload")
    public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {

        return s3Service.uploadImage(multipartFile);
    }
}