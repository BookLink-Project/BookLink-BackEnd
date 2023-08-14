package BookLink.BookLink.Domain.Book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookRentDto {

    private String title;
    private String authors;
    private String isbn;
    private LocalDate pub_date;
    private String cover;
    private String publisher;
    private Integer avg_rental_fee;
    private Integer rent_period;

    @Builder
    public BookRentDto(String title, String authors, String isbn, LocalDate pub_date, String cover, String publisher,
                       Integer avg_rental_fee, Integer rent_period) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.pub_date = pub_date;
        this.cover = cover;
        this.publisher = publisher;
        this.avg_rental_fee = avg_rental_fee;
        this.rent_period = rent_period;
    }
}
