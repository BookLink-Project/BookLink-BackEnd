package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Card.Card;
import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Common.SocialType;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.BookReply.BookReply;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@DynamicUpdate
@DynamicInsert
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email; // 아이디

    private String password;

    @NotNull
    private String nickname;

    private String name; // 사용자 실명

    private LocalDate birth; // 생년월일

    private String address;

    @ColumnDefault("'https://soccerquick.s3.ap-northeast-2.amazonaws.com/1689834239634.png'")
    private URL image; // 이미지 경로

    @ColumnDefault("'WEB'")
    private SocialType socialType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(mappedBy = "writer")
    private List<BookReply> bookReplies = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<BookReport> reports = new ArrayList<>();

//    @OneToMany(mappedBy = "writer")
//    private List<FreeBoard> freeBoards = new ArrayList<>();


//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "rent_id")
//    private List<BookRent> bookRent;

    @Builder
    public Member(String email, String password, String nickname, String name, LocalDate birth, String address, URL image, SocialType socialType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.image = image;
        this.socialType = socialType;
    }
}
