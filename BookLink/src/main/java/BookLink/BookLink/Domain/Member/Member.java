package BookLink.BookLink.Domain.Member;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.BookReply.BookReply;

import BookLink.BookLink.Domain.Common.Role;
import BookLink.BookLink.Domain.Message.Message;
import BookLink.BookLink.Domain.Message.MessageRoom;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
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
    private URL image;

//    private SocialType socialType; // 새로 추가

    private Role role; // 새로 추가

//    @ColumnDefault("0")
//    private Integer lend_cnt;
//
//    @ColumnDefault("0")
//    private Integer rent_cnt;
//
//    @ColumnDefault("0")
//    private Integer delay_return_cnt;

    @OneToMany(mappedBy = "writer")
    private List<BookReply> bookReplies = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<BookReport> reports = new ArrayList<>();

    @OneToMany(mappedBy = "writer", fetch = FetchType.EAGER)// LazyInitializationException 오류 해결하기 위해 EAGER 전략사용
    private List<Book> books = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<MessageRoom> sender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<MessageRoom> receiver = new ArrayList<>();

    public void updateAccount(URL image, String name, String nickname, String email, String password,
                                LocalDate birth, String address) {

        if (!password.equals("")) {
            this.password = password;
        }
        this.image = image;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.birth = birth;
        this.address = address;
//        this.card = card;
    }

    @Builder
    public Member(String email, String password, String nickname, String name,
                  LocalDate birth, String address, URL image, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.image = image;
        this.role = role;
    }

    @Builder
    public Member(String email, String password, String nickname, URL image) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
    }
}
