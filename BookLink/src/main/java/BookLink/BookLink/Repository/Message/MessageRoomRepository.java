package BookLink.BookLink.Repository.Message;

import BookLink.BookLink.Domain.Message.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
}
