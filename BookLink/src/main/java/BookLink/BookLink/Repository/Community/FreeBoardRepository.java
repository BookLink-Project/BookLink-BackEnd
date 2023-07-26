package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

}
