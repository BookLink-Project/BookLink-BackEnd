package BookLink.BookLink.Repository.CommunityReply.BookClub;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookClubReplyRepository extends JpaRepository<BookClubReply, Long> {

    List<BookClubReply> findByPostOrderByParentDescIdDesc(BookClub post);

    Long countByParentId(Long parentId);

    Optional<BookClubReply> findByIdAndPostId(Long replyId, Long postId);

    List<BookClubReply> findByWriterOrderByCreatedTimeDesc(Member member);

//    @Query(value = "SELECT * FROM book_club_reply UNION SELECT * FROM book_report_reply UNION SELECT * FROM free_board_reply",
//            nativeQuery = true)
//    List<Object[]> findAllCommunityReply();

}
