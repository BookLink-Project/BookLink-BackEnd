package BookLink.BookLink.Repository.CommunityReply.FreeBoard;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReply;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FreeBoardReplyRepository extends JpaRepository<FreeBoardReply, Long> {

    Optional<FreeBoardReply> findByIdAndPostId(Long replyId, Long postId);

    List<FreeBoardReply> findByPostOrderByParentDescIdDesc(FreeBoard post);

    Long countByParentId(Long parentId);

    List<FreeBoardReply> findByWriterOrderByCreatedTimeDesc(Member member);

}
