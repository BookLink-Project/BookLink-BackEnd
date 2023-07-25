package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookReport;
import BookLink.BookLink.Domain.Community.BookReportLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReportLikeRepository extends JpaRepository<BookReportLike, Long> {

    boolean existsByMemberAndPost(Member member, BookReport post);
}
