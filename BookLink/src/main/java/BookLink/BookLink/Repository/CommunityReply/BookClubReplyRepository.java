package BookLink.BookLink.Repository.CommunityReply;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookClubReplyRepository extends JpaRepository<BookClubReply, Long> {

    List<BookClubReply> findByPostOrderByParentDescIdDesc(BookClub post);

    Long countByParentId(Long parentId);

    Optional<BookClubReply> findByIdAndPostId(Long replyId, Long postId);
}
