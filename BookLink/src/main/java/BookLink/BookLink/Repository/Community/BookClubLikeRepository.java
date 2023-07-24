package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.Community.BookClubLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClubLikeRepository extends JpaRepository<BookClubLike, Long> {

    boolean existsByMemberAndPost(Member member, BookClub post);

}
