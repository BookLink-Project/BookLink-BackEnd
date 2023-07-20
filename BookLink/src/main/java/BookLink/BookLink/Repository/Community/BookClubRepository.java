package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookClubRepository extends JpaRepository<BookClub, Long> {

    List<BookClub> findAll();
}
