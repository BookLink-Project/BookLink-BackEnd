package BookLink.BookLink.Domain.BookReply;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert // for default 적용
@DynamicUpdate // 변경 필드만 update 반영
public class BookReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Long bookId;
     */
    @NotNull
    private String isbn;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    @NotNull
    private String content;

    @ColumnDefault("0")
    private Long like_cnt;

    @ManyToOne
    @JoinColumn(name = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookReply parent; // 가짜 매핑 X

    @ColumnDefault("false")
    private boolean isUpdated;

    public void updateParent(BookReply parent) {
        this.parent = parent;
    }

    public void updateReply(String content) {
        this.content = content;
        this.isUpdated = true;
    }

    public void increaseLikeCnt() {
        this.like_cnt += 1;
    }

    public void decreaseLikeCnt() {
        this.like_cnt -= 1;
    }

    @Builder
    public BookReply(String isbn, Member writer, String content, BookReply parent) {
        this.isbn = isbn;
        this.writer = writer;
        this.content = content;
        this.parent = parent;

        this.like_cnt = 0L;
        this.isUpdated = false;

    }

}
