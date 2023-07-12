package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, Long> {

    Long countByIsbn(String isbn);

}
