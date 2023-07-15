package BookLink.BookLink.Repository.BookReply;

import BookLink.BookLink.Domain.BookReply.BookReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookReplyRepository extends JpaRepository<BookReply, Long> {

    Long countByIsbn(String isbn);

    Long countByParentId(Long parent);

    List<BookReply> findByIsbnOrderByParentAscIdAsc(String isbn);

//    Optional<BookReply> findByIdAndParent(Long parent);

}
