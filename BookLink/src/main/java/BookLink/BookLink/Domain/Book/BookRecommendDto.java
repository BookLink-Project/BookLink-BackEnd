package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRecommendDto {

    private List<Item> item = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String isbn13;
        private String title;
        private String author;
        private String publisher;
        private LocalDate pubDate;
        private Integer priceStandard;
        private String cover;
    }

}
