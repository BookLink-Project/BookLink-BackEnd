package BookLink.BookLink.Domain.CommunityReply.BookReportReply;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert // for default 적용
@DynamicUpdate // 변경 필드만 update 반영
public class BookReportReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookReport post;

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
    private BookReportReply parent;

    @ColumnDefault("false")
    private boolean isUpdated;

    @Builder
    public BookReportReply(BookReport post, Member writer, String content , BookReportReply parent) {
        this.post = post;
        this.writer = writer;
        this.content = content;
        this.parent = parent;

        this.like_cnt = 0L;
        this.isUpdated = false;
    }

    public void updateParent(BookReportReply parent) {
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
}
