package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookRentListDto {

    private String title;
    private String authors;
    private String cover;
    private String publisher;
    private Integer avg_rental_fee;
    private Integer rent_period;

    @Builder
    public BookRentListDto(String title, String authors, String cover, String publisher, Integer avg_rental_fee, Integer rent_period) {
        this.title = title;
        this.authors = authors;
        this.cover = cover;
        this.publisher = publisher;
        this.avg_rental_fee = avg_rental_fee;
        this.rent_period = rent_period;
    }
}
