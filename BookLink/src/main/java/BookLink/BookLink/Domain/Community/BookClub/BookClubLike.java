package BookLink.BookLink.Domain.Community.BookClub;


import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_post_mem",
                        columnNames = {"post_id", "member_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class BookClubLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookClub post; // 가짜 매핑 X

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

    @Builder
    public BookClubLike(BookClub post, Member member) {
        this.post = post;
        this.member = member;
    }

}
