package BookLink.BookLink.Domain.OAuth;

import BookLink.BookLink.Domain.Common.SocialType;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Repository.Member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public SocialMemberInfoDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException, MalformedURLException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        KakaoTokenDto tokenDto = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        SocialMemberInfoDto kakaoMemberInfo = getKakaoMemberInfo(tokenDto.getAccess_token());

        // 3. 카카오ID로 회원가입 처리
        Member kakaoMember = registerKakaoMemberIfNeed(kakaoMemberInfo);

        // 4. 강제 로그인 처리
//        Authentication authentication = forceLogin(kakaoMember);

        // 5. response Header에 JWT 토큰 추가

//        kakaoMembersAuthorizationInput(authentication, response);
        Cookie access_cookie = new Cookie("Access_Token", tokenDto.getAccess_token());
        Cookie refresh_cookie = new Cookie("Refresh_Token", tokenDto.getRefresh_token());

        response.addCookie(access_cookie);
        response.addCookie(refresh_cookie);


        return kakaoMemberInfo;
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private KakaoTokenDto getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "7f52ecc5d9d0b4e210555d30a8555791");
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String access_token = jsonNode.get("access_token").asText();
        String refresh_token = jsonNode.get("refresh_token").asText();

        KakaoTokenDto kakaoTokenDto = new KakaoTokenDto(access_token, refresh_token);

        return kakaoTokenDto;
    }

    // 2. 토큰으로 카카오 API 호출
    private SocialMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException, MalformedURLException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String image = jsonNode.get("properties").get("profile_image").asText();

        URL image_url = new URL(image);

        return new SocialMemberInfoDto(id, nickname, email, image_url);
    }

    // 3. 카카오ID로 회원가입 처리
    private Member registerKakaoMemberIfNeed (SocialMemberInfoDto kakaoMemberInfo) {
        // DB 에 중복된 email이 있는지 확인
        String kakaoEmail = kakaoMemberInfo.getEmail();
        String nickname = kakaoMemberInfo.getNickname();
        URL image = kakaoMemberInfo.getImage();
        Member kakaoMember = memberRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoMember == null) {
            // 회원가입
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

//            String profile = "https://ossack.s3.ap-northeast-2.amazonaws.com/basicprofile.png";

//            kakaoMember = new Member(kakaoEmail, nickname, profile, encodedPassword);
            kakaoMember = new Member(kakaoEmail, encodedPassword, nickname, image, SocialType.KAKAO);
            memberRepository.save(kakaoMember);

        }
        return kakaoMember;
    }

//    // 4. 강제 로그인 처리
//    private Authentication forceLogin(Member kakaoMember) {
//        UserDetails userDetails = new UserDetailsImpl(kakaoMember);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return authentication;
//    }

//    // 5. response Header에 JWT 토큰 추가
//    private void kakaoMembersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
//        // response header에 token 추가
//        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
//        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
//        response.addHeader("Authorization", "BEARER" + " " + token);
//    }

}
