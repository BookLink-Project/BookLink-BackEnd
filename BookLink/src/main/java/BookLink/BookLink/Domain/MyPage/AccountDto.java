package BookLink.BookLink.Domain.MyPage;

import BookLink.BookLink.Domain.Card.Card;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.net.URL;
import java.time.LocalDate;

public class AccountDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private URL image;
        private String name;
        private String nickname;
        private String email;
        private String password;
        private LocalDate birth;
        private String address;
//        private Card card; // ???
    }

    @Getter
    @Setter
    @SuperBuilder
    @AllArgsConstructor
    public static class Response {
        private URL image;
        private String name;
        private String nickname;
        private String email;
        private LocalDate birth;
        private String address;
//        private Card card; // ???
    }

}
