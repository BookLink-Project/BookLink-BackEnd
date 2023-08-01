package BookLink.BookLink.Service.OAuth.MemberInfo;

import java.net.URL;
import java.util.Map;

// DTO
public abstract class OAuth2MemberInfo {

    protected Map<String, Object> attributes;

    public OAuth2MemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract URL getImageUrl();

//    public abstract String getEmail();

}
