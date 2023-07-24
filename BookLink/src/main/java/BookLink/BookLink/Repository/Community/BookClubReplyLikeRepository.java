package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookClubReplyLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClubReplyLikeRepository extends JpaRepository<BookClubReplyLike, Long> {


    boolean existsByMemberAndReply(Member member, BookClubReply reply);
}
