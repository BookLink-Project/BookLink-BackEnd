package BookLink.BookLink.Domain.Token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @NotNull
    private String memberEmail;

    public RefreshToken(String token, String email) {
        this.token = token;
        this.memberEmail = email;
    }

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }

}
