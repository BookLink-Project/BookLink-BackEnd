package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {


    Book findByIsbn(String isbn);

        boolean existsByIsbnAndWriter (String isbn, Member writer);

    @Query("SELECT DISTINCT title FROM Book")
    List<String> findDistinctTitles();

    List<Book> findByTitle(String title);

    List<Book> findByTitleContaining(String title);

    @Query("SELECT DISTINCT b.title FROM Book b GROUP BY b.title ORDER BY COUNT(b.title) DESC")
    List<String> findTitlesOrderByTitleCountDesc();

    @Query("SELECT DISTINCT b.title FROM Book b WHERE b.category_name = :category")
    List<String> findTitlesByCategory_name(@Param("category") String category);

    @Query("SELECT DISTINCT b.title FROM Book b WHERE b.category_name = :category GROUP BY b.title ORDER BY COUNT(b.title) DESC")
    List<String> findTitlesByCategory_nameCountDesc(@Param("category") String category);
  
    Long countByWriter(Member member);

//    @Query("SELECT COUNT(b) FROM Book b WHERE b.rent_signal = :rent_signal AND b.member_id = :member_id")
//    long countByRentSignalAndMemberId(@Param("rent_signal") boolean rentSignal, @Param("member_id") Long memberId);

    Long countByRentSignalAndWriter(boolean rentSignal, Member member);

    Long countByIsbn(String isbn);

}
