package BookLink.BookLink.Repository.Chat;

import BookLink.BookLink.Domain.Chat.ChatMessage;
import BookLink.BookLink.Domain.Chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
