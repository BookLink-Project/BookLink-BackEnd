package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRentRepository extends JpaRepository<BookRent, Long> {

    void deleteById(Long id);
    @Query("SELECT br FROM BookRent br WHERE br.rent_location LIKE :location%")
    List<BookRent> findByRentLocation(@Param("location") String location);

}
