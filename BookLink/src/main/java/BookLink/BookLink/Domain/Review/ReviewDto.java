package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Repository.Review.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Getter
@NoArgsConstructor
public class ReviewDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String content;
        private Long parentId;

        public Review toEntity(Member member, String isbn, Review parent) {

            return Review.builder()
                    .isbn(isbn)
                    .writer(member)
                    .content(content)
                    .parent(parent)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long reviewId;
    }



}
