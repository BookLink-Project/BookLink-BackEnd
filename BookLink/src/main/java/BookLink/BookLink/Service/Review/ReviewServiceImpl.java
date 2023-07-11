package BookLink.BookLink.Service.Review;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Review.Review;
import BookLink.BookLink.Domain.Review.ReviewDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ResponseDto writeReview(String memEmail, String isbn, ReviewDto.Request reviewDto) {

        /*
        Long bookId = bookRepository.findByIsbn(isbn);
        System.out.println("bookId = " + bookId);
         */

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        Review savedReview;

        if (reviewDto.getParentId() != 0) { // 자식 댓글의 경우 parent 찾기

            Review parent = reviewRepository.findById(reviewDto.getParentId()).orElse(null);
            Review review = reviewDto.toEntity(loginMember, isbn, parent);
            savedReview = reviewRepository.save(review);

        } else { // 부모 댓글의 경우 불필요한 쿼리 날리지 않기 위해 null

            Review review = reviewDto.toEntity(loginMember, isbn, null);
            savedReview = reviewRepository.save(review);

            // dirty checking
            Review updateReview = reviewRepository.findById(review.getId()).orElse(new Review());
            updateReview.updateParent(savedReview);

        }

        ReviewDto.Response responseData = new ReviewDto.Response(savedReview.getId());

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("성공");
        responseDto.setStatus(HttpStatus.OK);
        responseDto.setData(responseData);

        return responseDto;
    }
}
