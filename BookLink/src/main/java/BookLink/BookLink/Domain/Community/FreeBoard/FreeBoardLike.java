package BookLink.BookLink.Domain.Community.FreeBoard;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private FreeBoard post; // 가짜 매핑 X

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

    @Builder
    public FreeBoardLike(FreeBoard post, Member member) {
        this.post = post;
        this.member = member;
    }
}
