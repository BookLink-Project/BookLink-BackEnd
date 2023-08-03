package BookLink.BookLink.Domain.OAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoTokenDto {

    private String access_token;
    private String refresh_token;

}
