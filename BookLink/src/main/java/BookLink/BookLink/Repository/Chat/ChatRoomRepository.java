package BookLink.BookLink.Repository.Chat;

import BookLink.BookLink.Domain.Chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
