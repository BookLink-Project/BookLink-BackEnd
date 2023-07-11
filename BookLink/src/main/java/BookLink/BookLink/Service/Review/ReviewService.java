package BookLink.BookLink.Service.Review;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Review.ReviewDto;

public interface ReviewService {

    ResponseDto writeReview(String memEmail, String isbn, ReviewDto.Request reviewDto);

}
