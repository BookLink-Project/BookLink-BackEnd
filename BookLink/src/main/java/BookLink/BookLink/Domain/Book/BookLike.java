package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class BookLike {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String isbn; // 도서 API 의 외래키라고 생각

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

//    private Long like_cnt;
}
