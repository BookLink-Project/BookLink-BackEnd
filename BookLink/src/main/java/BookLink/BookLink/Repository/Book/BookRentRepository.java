package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRentRepository extends JpaRepository<BookRent, Long>   {
}
