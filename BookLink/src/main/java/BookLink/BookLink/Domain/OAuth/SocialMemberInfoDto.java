package BookLink.BookLink.Domain.OAuth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;

@Getter
@NoArgsConstructor
public class SocialMemberInfoDto {

    private Long id;
    private String nickname;
    private String email;
    private URL image;

    public SocialMemberInfoDto(Long id, String nickname, String email, URL image) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
    }
}
