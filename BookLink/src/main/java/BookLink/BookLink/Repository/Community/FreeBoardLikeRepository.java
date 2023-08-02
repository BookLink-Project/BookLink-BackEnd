package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.BookReport.BookReportLike;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardLikeRepository extends JpaRepository<FreeBoardLike, Long> {

    boolean existsByMemberAndPost(Member member, FreeBoard post);

    Optional<FreeBoardLike> findByMemberAndPost(Member member, FreeBoard post);
}
