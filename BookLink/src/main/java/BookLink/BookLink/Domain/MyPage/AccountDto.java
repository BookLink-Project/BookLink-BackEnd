package BookLink.BookLink.Domain.MyPage;

import lombok.*;

import java.net.URL;
import java.time.LocalDate;

public class AccountDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String name;
        private String nickname;
        private String email;
        private String password;
        private LocalDate birth;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private URL image;
        private String name;
        private String nickname;
        private String email;
        private LocalDate birth;
        private String address;
    }
}
