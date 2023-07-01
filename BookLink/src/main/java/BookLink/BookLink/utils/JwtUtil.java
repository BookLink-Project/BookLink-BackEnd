package BookLink.BookLink.utils;

import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    // private Key key;

    private static final Long expired_access = 1000 * 60 * 30L; // 30 minute
    private static final Long expired_refresh = 1000 * 60 * 60 * 24L; // 1 day

    private final RefreshTokenRepository refreshTokenRepository;


    /*
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // this.key = Keys.hmacShaKeyFor(bytes);
        this.key = new SecretKeySpec(bytes, "HmacSHA256");
    }
    */

    public String getEmailFromToken(String token) {
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

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getCookieToken(HttpServletRequest request, String type) {

        String token = type.equals("Access") ? "Access_Token" : "Refresh_Token";

        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(token))
                .findFirst().map(Cookie::getValue)
                .orElse(null);
    }

    public void setCookieAccessToken(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("Access_Token", accessToken);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30일

        response.addCookie(cookie);
    }

    public void setCookieRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("Refresh_Token", refreshToken);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30일

        response.addCookie(cookie);
    }
    /*
    public String getHeaderToken(HttpServletRequest request, String type) { // 헤더에서 Token 가져오기
        return type.equals("Access") ? request.getHeader("Access_Token") : request.getHeader("Refresh_Token");
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }
    */
}
