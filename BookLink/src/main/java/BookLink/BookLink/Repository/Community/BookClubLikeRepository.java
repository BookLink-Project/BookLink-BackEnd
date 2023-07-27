package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Community.BookClub.BookClubLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClubLikeRepository extends JpaRepository<BookClubLike, Long> {

    boolean existsByMemberAndPost(Member member, BookClub post);

    BookClubLike findByPostAndMember(BookClub post, Member member);

    Long countByPost(BookClub post);

}
