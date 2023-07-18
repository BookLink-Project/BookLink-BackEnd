package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookListDto {

    List<Item> item =  new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String isbn13;
        private String title;
        private String author;
        private String publisher;
        private String priceStandard;
        private String cover;

        private Long like_cnt;
        private Long reply_cnt;
        private Long owner_cnt;
    }

}
