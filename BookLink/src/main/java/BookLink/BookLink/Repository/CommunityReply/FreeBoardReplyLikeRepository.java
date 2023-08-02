package BookLink.BookLink.Repository.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyLike;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReply;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardReplyLikeRepository extends JpaRepository<FreeBoardReplyLike, Long> {

    boolean existsByMemberAndReply(Member member, FreeBoardReply reply);

    Optional<FreeBoardReplyLike> findByMemberAndReply(Member loginMember, FreeBoardReply reply);
}
