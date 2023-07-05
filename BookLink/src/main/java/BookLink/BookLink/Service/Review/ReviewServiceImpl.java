package BookLink.BookLink.Service.Review;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Review.Review;
import BookLink.BookLink.Domain.Review.ReviewDto;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ResponseDto writeReview(String memEmail, String isbn, ReviewDto reviewDto) {

        /*
        Long bookId = bookRepository.findByIsbn(isbn);
        System.out.println("bookId = " + bookId);
         */

        Optional<Member> loginMember = memberRepository.findByEmail(memEmail);

        Review review = reviewDto.toEntity(loginMember.orElse(null), isbn, reviewDto);

        reviewRepository.save(review);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("성공");
        responseDto.setStatus(HttpStatus.OK);

        return responseDto;
    }
}
