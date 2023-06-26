package BookLink.BookLink.Config;

import BookLink.BookLink.Service.MemberService.MemberService;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter { // 토큰을 매번 인증해야 함

    private final MemberService memberService;
    private final String secretKey;

    @Override // 얘가 문이라고 생각하면 됨. 여기서 권한 부여 가능.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization: {}", authorization);

        // Token 전송 여부 체크
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Authorization: 잘못된 형식입니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1]; // Token 꺼내기

        // Token Expired 여부 체크
        if (JwtUtil.isExpired(token, secretKey)) {
            log.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Token에서 email 꺼내기
        String email = JwtUtil.getEmail(token, secretKey);
        log.info("Email: {}", email);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("USER")));

        // detail 넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
