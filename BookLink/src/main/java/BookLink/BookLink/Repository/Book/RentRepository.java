package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Rent;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByLender(Member lender);

    List<Rent> findByRenter(Member renter);
}
