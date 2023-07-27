package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookLikeRepository extends JpaRepository<BookLike, Long> {

    Long countByIsbn(String isbn);

    Optional<BookLike> findByMemberAndIsbn(Member member, String isbn);

//    boolean existsByIsbnAndMember(String isbn, Member member);z

    boolean existsByMemberAndIsbn(Member member, String isbn);
}
