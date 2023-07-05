package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Card.Card;
import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Review.Review;
import lombok.*;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "member_id")
    private Long member_id;

    private String email; // 아이디

    private String password;

    private String nickname;

    private String name; // 사용자 실명

    private LocalDate birth; // 생년월일

    private String address;

    private URL image; // 이미지 경로

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(mappedBy = "writer") // 가짜 매핑
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, String name, LocalDate birth, String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.address = address;
    }
}
