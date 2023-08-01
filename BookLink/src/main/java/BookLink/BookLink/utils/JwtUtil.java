package BookLink.BookLink.utils;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import BookLink.BookLink.Service.Member.MemberPrincipalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final Long expired_access = 1000 * 60 * 60 * 24 * 3L; // 3 day
    private static final Long expired_refresh = 1000 * 60 * 60 * 24 * 15L; // 15 day

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberPrincipalService memberPrincipalService;

//    @PostConstruct
//    public void init() { // 객체 초기화 및 secretKey 인코딩
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }

    public String getEmailFromToken(String token) { // 토큰에서 이메일 추출
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("email", String.class);
    }

    public Boolean tokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error("tokenValid error = {}", ex.getMessage());
            return false;
        }
    }

    public Boolean refreshTokenValid(String token) { // DB와 비교

        if (!tokenValid(token)) return false; // 1차 검증

        String email = getEmailFromToken(token);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(email);

        return refreshToken.isPresent() && token.equals(refreshToken.get().getToken());
    }

    public TokenDto createAllToken(String email) {
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }

    public String createToken(String email, String type) {

        Claims claims = Jwts.claims(); // 일종의 Map
        claims.put("email", email);

        long time = type.equals("Access") ? expired_access : expired_refresh;

        return Jwts.builder() // JWT 토큰을 생성하는 빌더 반환
                .setClaims(claims)  // 클레임으로 email 식별자 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time)) // 토큰 만료시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘 사용
                .compact();
    }

    public String getCookieToken(HttpServletRequest request, String type) { // 쿠키에서 토큰 뽑기

        String token = type.equals("Access") ? "Access_Token" : "Refresh_Token";

        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(token))
                .findFirst().map(Cookie::getValue)
                .orElse(null);
    }

    public Authentication getAuthentication(String token) { // 토큰에서 인증 정보 조회 ???
        UserDetails userDetails = memberPrincipalService.loadUserByUsername(this.getEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                List.of(new SimpleGrantedAuthority("USER")));
    }

    public void setCookieAccessToken(HttpServletResponse response, String accessToken) {

        Cookie cookie = new Cookie("Access_Token", accessToken);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 3); // 3 day

        response.addCookie(cookie);
    }

    public void setCookieRefreshToken(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie("Refresh_Token", refreshToken);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 15); // 15 day

        response.addCookie(cookie);
    }

    public void removeTokenCookies(HttpServletResponse response) throws IOException {

        Cookie access_cookie = new Cookie("Access_Token", null);
        Cookie refresh_cookie = new Cookie("Refresh_Token", null);

        access_cookie.setPath("/");
        refresh_cookie.setPath("/");

        access_cookie.setMaxAge(0);
        refresh_cookie.setMaxAge(0);

        response.addCookie(access_cookie);
        response.addCookie(refresh_cookie);

        // 메시지 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.UNAUTHORIZED);
        responseDto.setMessage("토큰 만료");

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(responseDto);

        PrintWriter writer = response.getWriter();
        writer.print(result);

    }
}