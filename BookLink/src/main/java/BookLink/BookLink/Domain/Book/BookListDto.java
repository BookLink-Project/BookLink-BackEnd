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

        private int likes; // 좋아요 수
        private int reviews; // 댓글 수
        private int owners; // 소장자 수
    }

}
