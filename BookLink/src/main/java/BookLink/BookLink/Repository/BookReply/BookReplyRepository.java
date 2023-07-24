package BookLink.BookLink.Repository.BookReply;

import BookLink.BookLink.Domain.BookReply.BookReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookReplyRepository extends JpaRepository<BookReply, Long> {

    Long countByIsbn(String isbn);

    Long countByParentId(Long parent);

    List<BookReply> findByIsbnOrderByParentDescIdDesc(String isbn);

//    Optional<BookReply> findByIdAndParent(Long parent);

    Optional<BookReply> findByIdAndIsbn(Long id, String isbn);

//    @Query("SELECT br.lastModifiedTime FROM BookReply br WHERE br.id = :id")
//    LocalDateTime findLastModifiedTimeById(@Param("id")Long id);

}
