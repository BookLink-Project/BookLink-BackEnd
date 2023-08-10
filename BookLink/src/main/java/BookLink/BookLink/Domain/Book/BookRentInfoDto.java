package BookLink.BookLink.Domain.Book;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookRentInfoDto {

    private String isbn;
    private String writer;
    private LocalDateTime created_time;
    private String book_rating;
    private Integer rental_fee;
    private Integer max_date;
    private String rent_location;

    @Builder
    public BookRentInfoDto(String isbn, String writer, LocalDateTime created_time, String book_rating,
                           Integer rental_fee, Integer max_date, String rent_location) {
        this.isbn = isbn;
        this.writer = writer;
        this.created_time = created_time;
        this.book_rating = book_rating;
        this.rental_fee = rental_fee;
        this.max_date = max_date;
        this.rent_location = rent_location;
    }
}
