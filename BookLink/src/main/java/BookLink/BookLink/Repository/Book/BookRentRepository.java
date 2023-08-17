package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRentRepository extends JpaRepository<BookRent, Long> {

    void deleteById(Long id);
}
