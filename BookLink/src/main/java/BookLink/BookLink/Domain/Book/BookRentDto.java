package BookLink.BookLink.Domain.Book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookRentDto {

    private String title;
    private String authors;
    private String isbn;
    private String cover;
    private String publisher;
    private Integer avg_rental_fee;
    private Integer rent_period;

    @Builder
    public BookRentDto(String title, String authors, String isbn, String cover, String publisher, Integer avg_rental_fee, Integer rent_period) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.cover = cover;
        this.publisher = publisher;
        this.avg_rental_fee = avg_rental_fee;
        this.rent_period = rent_period;
    }
}
