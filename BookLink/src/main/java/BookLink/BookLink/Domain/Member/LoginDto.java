package BookLink.BookLink.Domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

public class LoginDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String email;

        private String password;
    }
}
