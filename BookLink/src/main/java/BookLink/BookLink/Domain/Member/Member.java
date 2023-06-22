package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Card.Card;
import lombok.*;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    private String email; // 아이디

    private String password;

    private String nickname;

    private String name; // 사용자 실명

    private Date birth; // 생년월일

    private String address;

    private URL image; // 이미지 경로

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id")
    private Card card;

    @Builder
    public Member(String email, String password, String nickname, String name, Date birth, String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.address = address;
    }
}
