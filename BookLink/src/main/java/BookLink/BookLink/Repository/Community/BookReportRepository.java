package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReportRepository extends JpaRepository<BookReport, Long> {
}
