package BookLink.BookLink.Domain.CommunityReply.BookClubReply;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Community.BookClub.BookClub;
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
public class BookClubReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookClub post;

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
    private BookClubReply parent; // 가짜 매핑 X

    @ColumnDefault("false")
    private boolean isUpdated;

    public void updateParent(BookClubReply parent) {
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
    public BookClubReply(BookClub post, Member writer, String content, BookClubReply parent) {
        this.post = post;
        this.writer = writer;
        this.content = content;
        this.parent = parent;

        this.like_cnt = 0L;
        this.isUpdated = false;

    }

}
