package BookLink.BookLink.Domain.MyPage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyBookLendDto {

    private String title;
    private String authors;
    private String publisher;
    private String renter;
    private String rent_location;
    private LocalDateTime rent_date;
    private String return_location;
    private LocalDateTime return_date;
    private Integer rental_fee;

    @Builder
    public MyBookLendDto(String title, String authors, String publisher, String renter, String rent_location, LocalDateTime rent_date,
                         String return_location, LocalDateTime return_date, Integer rental_fee) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.renter = renter;
        this.rent_location = rent_location;
        this.rent_date = rent_date;
        this.return_location = return_location;
        this.return_date = return_date;
        this.rental_fee = rental_fee;
    }
}
