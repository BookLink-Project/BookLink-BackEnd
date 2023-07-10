package BookLink.BookLink.Domain.Review;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Date;

@Getter
public class ReviewDto { // ???

    // private String bookId; // URI
    private String content;

    public Review toEntity(Member member, String isbn) {

        return Review.builder()
                .isbn(isbn)
                .writer(member)
                .date(new Date(System.currentTimeMillis()))
                .content(content)
                .build();
    }

}
