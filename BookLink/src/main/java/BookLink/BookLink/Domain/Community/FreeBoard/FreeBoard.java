package BookLink.BookLink.Domain.Community.FreeBoard;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
//@DynamicInsert
//@DynamicUpdate
public class FreeBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ColumnDefault("false")
    private boolean isUpdated;

    @Builder
    public FreeBoard(Member writer, String category, String title, String content) {
        this.writer = writer;
        this.category = category;
        this.title = title;
        this.content = content;

        this.like_cnt = 0L;
        this.view_cnt = 0L;
        this.reply_cnt = 0L;

        this.isUpdated = false;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        this.isUpdated = true;
    }

    public void view_plus() {
        this.view_cnt += 1;
    }

    public void like_plus() { this.like_cnt += 1; }

    public void like_minus() { this.like_cnt -= 1; }

    public void replyCnt_plus() {
        this.reply_cnt += 1;
    }

    public void replyCnt_minus(Long cnt) {
        this.reply_cnt -= cnt;
    }
}
