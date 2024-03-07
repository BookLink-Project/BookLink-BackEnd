package BookLink.BookLink.Domain.MyPage;

import BookLink.BookLink.Domain.Common.RentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyRecordBookDto {

    private Long book_id;
    private RentStatus rent_status;
    private String cover;
    private String title;
    private String authors;
    private String publisher;
    private Integer rental_fee;
    private Integer max_date;

    @Builder
    public MyRecordBookDto(Long book_id, RentStatus rent_status, String cover, String title, String authors, String publisher,
                           Integer rental_fee, Integer max_date) {
        this.book_id = book_id;
        this.rent_status = rent_status;
        this.cover = cover;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.rental_fee = rental_fee;
        this.max_date = max_date;
    }
}
