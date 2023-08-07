package BookLink.BookLink.Repository.Community.FreeBoard;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    List<FreeBoard> findByWriterOrderByCreatedTimeDesc(Member member);

}
