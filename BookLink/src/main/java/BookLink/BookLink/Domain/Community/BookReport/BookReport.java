package BookLink.BookLink.Domain.Community.BookReport;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class BookReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String book_title;

    @NotNull
    private String isbn;

    @NotNull
    private String authors;

    @NotNull
    private String publisher;

    @NotNull
    private LocalDate pud_date;

    @NotNull
    private String cover; // 도서 표지 미리보기 URL

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @ColumnDefault("0")
    private Long like_cnt;

    @ColumnDefault("0")
    private Long view_cnt;

    @ColumnDefault("0")
    private Long reply_cnt;

    @Builder
    public BookReport(String book_title, String isbn, String authors, String publisher, LocalDate pud_date, String cover, String category,
                      String title, String content, Member writer) {
        this.book_title = book_title;
        this.isbn = isbn;
        this.authors = authors;
        this.publisher = publisher;
        this.pud_date = pud_date;
        this.cover = cover;
        this.category = category;
        this.title = title;
        this.content = content;
        this.writer = writer;

        this.like_cnt = 0L;
        this.view_cnt = 0L;
        this.reply_cnt = 0L;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void view_plus() {
        this.view_cnt += 1;
    }

    public void like_plus() { this.like_cnt += 1; }

    public void like_minus() { this.like_cnt -= 1; }

    public void replyCnt_plus() {
        this.reply_cnt += 1;
    }

    public void replyCnt_minus() {
        this.reply_cnt -= 1;
    }



}
