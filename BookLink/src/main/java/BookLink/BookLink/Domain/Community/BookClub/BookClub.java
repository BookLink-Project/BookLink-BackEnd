package BookLink.BookLink.Domain.Community.BookClub;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class BookClub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ColumnDefault("0")
    private Long like_cnt;

    @ColumnDefault("0")
    private Long view_cnt;

    @ColumnDefault("0")
    private Long reply_cnt;

    @ColumnDefault("false")
    private boolean isUpdated;

    @Builder
    public BookClub(String title, String content, Member writer, String location) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.location = location;

        this.like_cnt = 0L;
        this.view_cnt = 0L;
        this.reply_cnt = 0L;

        this.isUpdated = false;
    }

    public void increaseReplyCnt() {
        this.reply_cnt += 1;
    }

    public void decreaseReplyCnt() {
        this.reply_cnt -= 1;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        this.isUpdated = true;
    }
}
