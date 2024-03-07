package BookLink.BookLink.Repository.CommunityReply.BookReport;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReportReplyLikeRepository extends JpaRepository<BookReportReplyLike, Long> {

    boolean existsByMemberAndReply(Member member, BookReportReply reply);

    Optional<BookReportReplyLike> findByMemberAndReply(Member loginMember, BookReportReply reply);
}
