package BookLink.BookLink.Repository.Community.BookClub;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookClubRepository extends JpaRepository<BookClub, Long> {

    List<BookClub> findAll();

    boolean existsById(Long id);

    List<BookClub> findByWriterOrderByCreatedTimeDesc(Member member);

}
