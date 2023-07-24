package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_book_mem",
                        columnNames = {"isbn", "member_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class BookLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String isbn; // 도서 API 의 외래키라고 생각

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 가짜 매핑 X

    @Builder
    public BookLike(String isbn, Member member) {
        this.isbn = isbn;
        this.member = member;
    }

}
