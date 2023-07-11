package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert // for default 적용
@DynamicUpdate // 변경 필드만 update 반영
public class Review extends BaseTimeEntity {

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

    @NotNull
    private String content;

//    @NotNull
    @ColumnDefault("0")
    private Long like_cnt;

//    @NotNull
    @ColumnDefault("0")
    private Long hates_cnt;

//    @NotNull // test
    @ManyToOne
    @JoinColumn(name = "parent")
    private Review parent; // 가짜 매핑 X

//    @NotNull // test
    private boolean isDeleted;

//    @NotNull
//    private boolean isModified;

    public void updateParent(Review parent) {
        this.parent = parent;
    }

    @Builder
    public Review(String isbn, Member writer, String content, Review parent) {
        this.isbn = isbn;
        this.writer = writer;
        this.content = content;
        this.parent = parent;
    }
}
