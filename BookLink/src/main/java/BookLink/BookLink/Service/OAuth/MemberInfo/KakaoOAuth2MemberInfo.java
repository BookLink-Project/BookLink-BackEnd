//package BookLink.BookLink.Service.OAuth.MemberInfo;
//
//import java.net.URL;
//import java.util.Map;
//
//public class KakaoOAuth2MemberInfo extends OAuth2MemberInfo {
//
//    public KakaoOAuth2MemberInfo(Map<String, Object> attributes) {
//        super(attributes);
//    }
//
//    @Override
//    public String getId() {
//        return String.valueOf(attributes.get("id"));
//    }
//
//    @Override
//    public String getNickname() {
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
//
//        if (account == null || profile == null) {
//            return null;
//        }
//
//        return (String) profile.get("nickname");
//    }
//
//    @Override
//    public URL getImageUrl() {
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
//
//        if (account == null || profile == null) {
//            return null;
//        }
//
//        return (URL) profile.get("thumbnail_image_url");
//    }
//}
//
