package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class BookClub extends BaseTimeEntity {

    @Id
    @GeneratedValue
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    @NotNull
    private String location;

    @NotNull
    private Long like_cnt;

    @NotNull
    private Long view_cnt;

    @NotNull
    private Long reply_cnt;

    @Builder
    public BookClub(String title, String content, Member writer, String location) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.location = location;

        this.like_cnt = 0L;
        this.view_cnt = 0L;
        this.reply_cnt = 0L;
    }
}
