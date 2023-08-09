package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
