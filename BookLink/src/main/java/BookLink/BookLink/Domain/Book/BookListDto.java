package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookListDto {

    List<Item> item =  new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Item {
        private String title;
        private String author;
        private String publisher;
        private String priceStandard;
        private String cover;
    }

}
