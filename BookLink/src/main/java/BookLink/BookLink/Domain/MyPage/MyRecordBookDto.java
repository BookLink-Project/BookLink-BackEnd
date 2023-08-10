package BookLink.BookLink.Domain.MyPage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyRecordBookDto {

    private String cover;
    private String title;
    private String authors;
    private String publisher;
    private Integer rental_fee;
    private Integer max_date;

    @Builder
    public MyRecordBookDto(String cover, String title, String authors, String publisher, Integer rental_fee, Integer max_date) {
        this.cover = cover;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.rental_fee = rental_fee;
        this.max_date = max_date;
    }
}
