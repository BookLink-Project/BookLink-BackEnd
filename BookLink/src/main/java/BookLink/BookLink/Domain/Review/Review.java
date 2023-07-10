package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Long bookId;
     */
    @NotNull
    private String isbn; // TODO foreign key in API?

    @NotNull
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @NotNull
    private String content;

    // 좋아요, 싫어요, 답글 추가 예정

    @Builder
    public Review(String isbn, Member writer, Date date, String content) {
        this.isbn = isbn;
        this.writer = writer;
        this.date = date;
        this.content = content;
    }
}
