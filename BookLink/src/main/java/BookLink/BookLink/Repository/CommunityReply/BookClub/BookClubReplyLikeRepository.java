package BookLink.BookLink.Repository.CommunityReply.BookClub;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookClubReplyLikeRepository extends JpaRepository<BookClubReplyLike, Long> {

    boolean existsByMemberAndReply(Member member, BookClubReply reply);

    Optional<BookClubReplyLike> findByMemberAndReply(Member member, BookClubReply reply);

}
