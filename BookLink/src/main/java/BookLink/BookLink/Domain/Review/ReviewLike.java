package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ReviewLike {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

    @NotNull
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review; // 가짜 매핑 X

}
