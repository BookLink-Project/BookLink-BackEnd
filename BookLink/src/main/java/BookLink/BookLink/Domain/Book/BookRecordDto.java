package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.RentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookRecordDto {

    private RentStatus rent_status;
    private String title;
    private String authors;
    private String isbn;
    private String cover;
    private LocalDateTime created_time;
    private String publisher;
    private Integer rental_fee;

    @Builder
    public BookRecordDto(RentStatus rent_status, String title, String authors, String isbn, String cover, LocalDateTime created_time,
                         String publisher, Integer rental_fee) {
        this.rent_status = rent_status;
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.cover = cover;
        this.created_time = created_time;
        this.publisher = publisher;
        this.rental_fee = rental_fee;
    }
}
