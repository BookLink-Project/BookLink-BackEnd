package BookLink.BookLink.Repository.Community.BookReport;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookReportRepository extends JpaRepository<BookReport, Long> {

    List<BookReport> findTop5ByIsbnOrderByCreatedTimeDesc(String isbn);

    List<BookReport> findByWriterOrderByCreatedTimeDesc(Member member);

    @Query("SELECT COUNT(DISTINCT br.isbn) FROM BookReport br WHERE br.writer = :writer")
    Long countIsbnByWriter(@Param("writer") Member writer);

}
