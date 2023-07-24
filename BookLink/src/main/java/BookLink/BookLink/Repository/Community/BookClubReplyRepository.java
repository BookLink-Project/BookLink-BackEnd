package BookLink.BookLink.Repository.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookClubReplyRepository extends JpaRepository<BookClubReply, Long> {

    List<BookClubReply> findByPostOrderByParentDescIdDesc(BookClub post);

    Long countByParentId(Long parentId);

}
