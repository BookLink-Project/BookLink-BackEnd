package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id")
    private Member lender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private Member renter;

    private LocalDateTime rent_date;

    private LocalDateTime return_date;

    private String return_location;

    @Builder
    public Rent(Book book, Member lender, Member renter, LocalDateTime rent_date, LocalDateTime return_date, String return_location) {
        this.book = book;
        this.lender = lender;
        this.renter = renter;
        this.rent_date = rent_date;
        this.return_date = return_date;
        this.return_location = return_location;
    }
}
