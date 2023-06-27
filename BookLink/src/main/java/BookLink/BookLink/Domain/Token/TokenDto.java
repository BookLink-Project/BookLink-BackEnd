package BookLink.BookLink.Domain.Token;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;

    private String refreshToken;
}
