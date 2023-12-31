package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Card.Card;
import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Community.FreeBoard;
import BookLink.BookLink.Domain.Review.Review;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email; // 아이디

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @NotNull
    private String name; // 사용자 실명

    @NotNull
    private LocalDate birth; // 생년월일

    @NotNull
    private String address;

    private URL image; // 이미지 경로

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(mappedBy = "writer")
    private List<Review> reviews = new ArrayList<>();

//    @OneToMany(mappedBy = "writer")
//    private List<FreeBoard> freeBoards =new ArrayList<>();


//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "rent_id")
//    private List<BookRent> bookRent;

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
