package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Getter;

@Getter
public class ReviewDto {

    private String content;

    public Review toEntity(Member member, String isbn) {

        return Review.builder()
                .isbn(isbn)
                .writer(member)
                .content(content)
                .build();
    }

}
