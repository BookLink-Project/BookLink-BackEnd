package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    int findByIsbn(String isbn);

    boolean existsByIsbnAndMember (String isbn, Member member);

    @Query("SELECT DISTINCT title FROM Book")
    List<String> findDistinctTitles();

    List<Book> findByTitle(String title);

    @Query("SELECT DISTINCT b.title FROM Book b GROUP BY b.title ORDER BY COUNT(b.title) DESC")
    List<String> findTitlesOrderByTitleCountDesc();

    @Query("SELECT DISTINCT b.title FROM Book b WHERE b.category_name = :category")
    List<String> findTitlesByCategory_name(String category);

    @Query("SELECT DISTINCT b.title FROM Book b WHERE b.category_name = :category GROUP BY b.title ORDER BY COUNT(b.title) DESC")
    List<String> findTitlesByCategory_nameCountDesc(String category);


}
