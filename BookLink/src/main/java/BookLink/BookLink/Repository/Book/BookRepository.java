package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {


}
