package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Common.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;

public class KakaoMemberDto {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private String is_default_image;

        public static Member toEntity(KakaoMemberDto.Request request, String email) throws MalformedURLException {
            return Member.builder()
                    .email(email)
                    .nickname(request.getNickname())
                    .image(toUrl(request.profile_image_url))
                    .build();
        }


    }
    public static URL toUrl(String profile_image_url) throws MalformedURLException {
        URL url = new URL(profile_image_url);
        return url;
    }


}
