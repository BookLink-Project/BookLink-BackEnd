package BookLink.BookLink.Repository.BookReply;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReplyLikeRepository extends JpaRepository<BookReplyLike, Long> {

    BookReplyLike findByReplyAndMember(BookReply reply, Member member);

}
