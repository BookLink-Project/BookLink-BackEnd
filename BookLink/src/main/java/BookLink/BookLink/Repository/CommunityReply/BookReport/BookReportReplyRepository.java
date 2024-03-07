package BookLink.BookLink.Repository.CommunityReply.BookReport;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookReportReplyRepository extends JpaRepository<BookReportReply, Long> {

    Optional<BookReportReply> findByIdAndPostId(Long replyId, Long postId);

    List<BookReportReply> findByPostOrderByParentDescIdDesc(BookReport post);

    Long countByParentId(Long parentId);

    List<BookReportReply> findByWriterOrderByCreatedTimeDesc(Member member);

}
