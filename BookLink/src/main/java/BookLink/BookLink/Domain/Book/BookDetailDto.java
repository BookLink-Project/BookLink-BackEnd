package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Review.Review;
import BookLink.BookLink.Domain.Review.ReviewsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailDto {

    List<Item> item = new ArrayList<>();
    List<ReviewsDto> reviews = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        private String isbn13;
        private String title;
        private String author;
        private String publisher;
        private String pubDate;
        private String description;
        private String cover;

        private Long like_cnt;
        private Long review_cnt; // 대댓글 제외 댓글 수
        private Long owner_cnt;

    }

}
