package BookLink.BookLink.Domain.Member;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;


public class MemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String email; // 아이디
        private String password;
        private String nickname;
        private String name; // 사용자 실명
        private LocalDate birth; // 생년월일
        private String address;

        public static Member toEntity(MemberDto.Request requestDto) {
            return Member.builder()
                    .email(requestDto.email)
                    .password(requestDto.password)
                    .nickname(requestDto.nickname)
                    .name(requestDto.name)
                    .birth(requestDto.birth)
                    .address(requestDto.address)
                    .build();
        }

        public void setEncodePwd(String encodePwd) {
            this.password = encodePwd;
        }
    }


}
