package BookLink.BookLink.Domain.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class EmailDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String email;
        private String authentication_number;

        public static Email toEntity(String email, String number) {
            return Email.builder()
                    .email(email)
                    .number(number)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String authentication_number;

    }

}
