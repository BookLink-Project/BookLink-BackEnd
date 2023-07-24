package BookLink.BookLink.Domain.CommunityReply;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_mem_rep",
                        columnNames = {"member_id", "reply_id"}
                )
        }
)
@NoArgsConstructor
public class BookClubReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reply_id")
    private BookClubReply reply; // 가짜 매핑 X

    @Builder
    public BookClubReplyLike(Member member, BookClubReply reply) {
        this.member = member;
        this.reply = reply;
    }

}
