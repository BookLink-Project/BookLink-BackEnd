package BookLink.BookLink.Domain.Kakao;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoUserInfo {

    private Long id;
    private String profile_nickname;
    private String account_email;
    private String birthday;

}
