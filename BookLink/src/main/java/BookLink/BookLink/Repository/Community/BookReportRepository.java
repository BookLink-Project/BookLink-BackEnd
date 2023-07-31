package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReportRepository extends JpaRepository<BookReport, Long> {

    List<BookReport> findTop5ByIsbnOrderByCreatedTimeDesc(String isbn);

}
