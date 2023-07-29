package BookLink.BookLink.Service.OAuth.handler;

import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import BookLink.BookLink.Service.OAuth.CustomOAuth2Member;
import BookLink.BookLink.Service.OAuth.Role;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if(oAuth2Member.getRole() == Role.GUEST) {
                TokenDto allToken = jwtUtil.createAllToken(oAuth2Member.getEmail());

                refreshTokenRepository.save(new RefreshToken(allToken.getRefreshToken(), oAuth2Member.getEmail()));

//                String accessToken = jwtUtil.createToken(oAuth2User.getEmail(), "Access");
//                response.addHeader(accessToken, "Bearer " + accessToken);
                response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

//                jwtService.sendAccessAndRefreshToken(response, accessToken, null);
                jwtUtil.setCookieAccessToken(response, allToken.getAccessToken());
                jwtUtil.setCookieRefreshToken(response, allToken.getRefreshToken());
//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();
            } else {
                loginSuccess(response, oAuth2Member); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }
        } catch (Exception e) {
            throw e;
        }

    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2Member) throws IOException {

        TokenDto allToken = jwtUtil.createAllToken(oAuth2Member.getEmail());
        jwtUtil.setCookieAccessToken(response, allToken.getAccessToken());
        jwtUtil.setCookieRefreshToken(response, allToken.getRefreshToken());

//        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
