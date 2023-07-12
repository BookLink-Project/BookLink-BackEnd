package BookLink.BookLink.Repository.Review;

import BookLink.BookLink.Domain.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Long countByIsbn(String isbn);

    Long countByParentId(Long parent);

    List<Review> findByIsbn(String isbn);

}
