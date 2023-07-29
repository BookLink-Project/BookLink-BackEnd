package BookLink.BookLink.Repository.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReportReplyLikeRepository extends JpaRepository<BookReportReplyLike, Long> {

    Optional<BookReportReplyLike> findByMemberAndReply(Member loginMember, BookReportReply reply);
}
