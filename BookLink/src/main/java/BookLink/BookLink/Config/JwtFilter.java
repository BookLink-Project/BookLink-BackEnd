package BookLink.BookLink.Config;

import BookLink.BookLink.Service.MemberService.MemberService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // 토큰을 매번 인증해야 함 TODO authorization test

    private final JwtUtil jwtUtil;

    @Override // 얘가 문이라고 생각하면 됨. 여기서 권한 부여 가능.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getHeaderToken(request, "Access"); // Access_Token
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh"); // Refresh_Token

        if(accessToken != null) {

            if (jwtUtil.tokenValid(accessToken)) { // Access Token 유효
                setAuthentication(request, accessToken);

            } else if (refreshToken != null) { // Access Token 만료 && Refresh Token 존재

                boolean isRefreshTokenValid = jwtUtil.refreshTokenValid(refreshToken);

                if (isRefreshTokenValid) { // Refresh Token 유효 (DB 정보와 일치)
                    String loginEmail = jwtUtil.getEmail(refreshToken);
                    String newAccessToken = jwtUtil.createToken(loginEmail, "Access");
                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
                    setAuthentication(request, newAccessToken);

                } else { // Refresh Token 만료 || DB 정보와 불일치
                    // jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(HttpServletRequest request, String token) {
        // SecurityContext에 Authentication 객체(인증 정보) 저장
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(jwtUtil.getEmail(token), null, List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /*
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
     */
}