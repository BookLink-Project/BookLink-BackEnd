package BookLink.BookLink.Repository.Review;

import BookLink.BookLink.Domain.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Long countByIsbn(String isbn);
}
