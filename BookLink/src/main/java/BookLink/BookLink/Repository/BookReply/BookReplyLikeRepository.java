package BookLink.BookLink.Repository.BookReply;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookReplyLikeRepository extends JpaRepository<BookReplyLike, Long> {

    BookReplyLike findByReplyAndMember(BookReply reply, Member member);

    // @Query("select id from book_reply_like where member_id = :memberId and reply_id = :replyId")
    boolean existsByMemberAndReply(Member member, BookReply reply);

}
