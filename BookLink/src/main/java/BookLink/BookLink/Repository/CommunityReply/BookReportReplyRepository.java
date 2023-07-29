package BookLink.BookLink.Repository.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReportReplyRepository extends JpaRepository<BookReportReply, Long> {

    Optional<BookReportReply> findByIdAndPostId(Long replyId, Long postId);


}
