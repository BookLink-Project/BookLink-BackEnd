package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRentRepository extends JpaRepository<BookRent, Long> {

    void deleteById(Long id);
    @Query(value = "SELECT * FROM book_rent WHERE rent_location LIKE :location% " +
                   "ORDER BY created_time DESC LIMIT 5", nativeQuery = true)
    List<BookRent> findTop5ByRentLocation(@Param("location") String location);

}
