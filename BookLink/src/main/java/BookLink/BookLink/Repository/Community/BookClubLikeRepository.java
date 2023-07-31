package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Community.BookClub.BookClubLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookClubLikeRepository extends JpaRepository<BookClubLike, Long> {

    boolean existsByMemberAndPost(Member member, BookClub post);

    Optional<BookClubLike> findByMemberAndPost(Member member, BookClub post);

    Long countByPost(BookClub post);

}
