package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.BookReply.BookRepliesDto;
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

    private List<Item> item = new ArrayList<>();
    private List<BookRepliesDto> replies = new ArrayList<>();
    private List<BookRecommendDto.Item> recommended_books;
    private List<BookRelatedPostDto> related_posts = new ArrayList<>();

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
        private Integer categoryId;
        private String categoryName;

        private Long like_cnt;
        private Long reply_cnt;
        private Long owner_cnt;

        private boolean isLiked;

    }

}
