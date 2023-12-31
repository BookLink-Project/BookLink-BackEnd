package BookLink.BookLink.Config;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // 토큰 매번 인증

    private final JwtUtil jwtUtil;

    @Override // 얘가 문이라고 생각하면 됨. 여기서 권한 부여 가능.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

         /*
         헤더에서 Token 가져오기
         String accessToken = jwtUtil.getHeaderToken(request, "Access");
         String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");
         */

        String accessToken = jwtUtil.getCookieToken(request, "Access");
        String refreshToken = jwtUtil.getCookieToken(request, "Refresh");

        if(accessToken != null) {

            if (jwtUtil.tokenValid(accessToken)) {
                log.info("Access Token 유효");
                setAuthentication(request, accessToken);

            } else if (refreshToken != null) {

                log.info("Access Token 만료");

                boolean isRefreshTokenValid = jwtUtil.refreshTokenValid(refreshToken);

                if (isRefreshTokenValid) {
                    log.info("Access Token 만료 + Refresh Token 유효");
                    String loginEmail = jwtUtil.getEmailFromToken(refreshToken);
                    String newAccessToken = jwtUtil.createToken(loginEmail, "Access");
                    jwtUtil.setCookieAccessToken(response, newAccessToken);
                    setAuthentication(request, newAccessToken);

                } else {

                    log.info("Access Token 만료 + Refresh Token 만료");

                    // TODO 로그아웃 처리

                    return;
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(HttpServletRequest request, String token) {
        // SecurityContext에 Authentication 객체(인증 정보) 저장
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(jwtUtil.getEmailFromToken(token), null, List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}