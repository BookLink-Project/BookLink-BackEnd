package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Book.Rent;
import BookLink.BookLink.Domain.Common.RentStatus;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByLender(Member lender);

    List<Rent> findByRenter(Member renter);

    Rent findByLenderAndBook(Member lender, Book book);

    Rent findByRenterAndBook(Member renter,Book book);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.renter = :renter AND r.rent_status = :rentStatus")
    Long countByRenterAndRentStatus(@Param("renter") Member renter,
                                     @Param("rentStatus") RentStatus rentStatus);

    @Query("SELECT COUNT(r) FROM Rent r WHERE r.lender = :lender AND r.rent_status = :rentStatus")
    Long countByRenterAndLendStatus(@Param("lender") Member lender,
                                     @Param("rentStatus") RentStatus rentStatus);

    Long countByRenter(Member renter);

    Long countByLender(Member renter);

}
