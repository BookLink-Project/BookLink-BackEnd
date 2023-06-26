package BookLink.BookLink.utils;

import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    private static final Long expired_access = 1000 * 60L; // 1 minute
    private static final Long expired_refresh = 1000 * 60L; // 1 minute

    private RefreshTokenRepository refreshTokenRepository;


    /*
    @PostConstruct // bean으로 등록 되면서 딱 한 번 실행
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // this.key = Keys.hmacShaKeyFor(bytes);
        this.key = new SecretKeySpec(bytes, "HmacSHA256");
    }
    */

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("email", String.class);
    }

    public Boolean tokenValid(String token) { // token 검증
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public Boolean refreshTokenValid(String token) { // DB와 비교

        if (!tokenValid(token)) return false;

        String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("email", String.class);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(email);

        return refreshToken.isPresent() && token.equals(refreshToken.get().getToken());
    }

    // 토큰 생성
    public TokenDto createAllToken(String email) {
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }

    public String createToken(String email, String type) {

        long time = type.equals("Access") ? expired_access : expired_refresh;

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getHeaderToken(HttpServletRequest request, String type) { // Token 가져오기
        return type.equals("Access") ? request.getHeader("Access_Token") : request.getHeader("Refresh_Token");
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }


}
