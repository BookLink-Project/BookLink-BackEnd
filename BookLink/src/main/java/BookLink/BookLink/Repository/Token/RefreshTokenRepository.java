package BookLink.BookLink.Repository.Token;

import BookLink.BookLink.Domain.Token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberEmail(String email);

    Optional<RefreshToken> findByToken(String token);

    void deleteByMemberEmail(String email);

}
