package BookLink.BookLink.Domain.CommunityReply.BookReportReply;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
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
@DynamicInsert // for default 적용
@DynamicUpdate // 변경 필드만 update 반영
public class BookReportReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
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
    private BookReportReply parent;

    @Builder
    public BookReportReply(BookReport post, Member writer, String content, Long like_cnt, BookReportReply parent) {
        this.post = post;
        this.writer = writer;
        this.content = content;
        this.like_cnt = like_cnt;
        this.parent = parent;
    }

    public void updateParent(BookReportReply parent) {
        this.parent = parent;
    }
}
