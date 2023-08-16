package BookLink.BookLink.Domain.Chat;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter")
    private Member renter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender")
    private Member lender;

    @Builder
    public ChatRoom(Book book, Member renter, Member lender) {
        this.book = book;
        this.renter = renter;
        this.lender = lender;
    }
}
