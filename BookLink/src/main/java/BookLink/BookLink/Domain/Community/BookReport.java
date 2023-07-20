package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReport extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String book_title;

    private String authors;

    private String publisher;

    private LocalDate pud_date;

    // 표지이미지

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    @JsonIgnore
    private Member writer;

    @Builder
    public BookReport(String book_title, String authors, String publisher, LocalDate pud_date, String title, String contents, Member writer) {
        this.book_title = book_title;
        this.authors = authors;
        this.publisher = publisher;
        this.pud_date = pud_date;
        this.title = title;
        this.content = contents;
        this.writer = writer;
    }
}
