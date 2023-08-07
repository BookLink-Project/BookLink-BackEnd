package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    int findByIsbn(String isbn);

    boolean existsByIsbnAndMember (String isbn, Member member);

    @Query("SELECT DISTINCT title FROM Book")
    List<String> findDistinctTitles();

    List<Book> findByTitle(String title);

    Long countByMember(Member member);

//    @Query("SELECT COUNT(b) FROM Book b WHERE b.rent_signal = :rent_signal AND b.member_id = :member_id")
//    long countByRentSignalAndMemberId(@Param("rent_signal") boolean rentSignal, @Param("member_id") Long memberId);

    Long countByRentSignalAndMember(boolean rentSignal, Member member);

}
