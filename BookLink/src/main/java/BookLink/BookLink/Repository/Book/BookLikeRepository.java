package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookLike;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, Long> {

    Long countByIsbn(String isbn);

    BookLike findByIsbnAndMember(String isbn, Member member);

    boolean existsByIsbnAndMember(String isbn, Member member);

    boolean existsByMemberAndIsbn(Member member, String isbn);
}
