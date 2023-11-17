package BookLink.BookLink.Repository.Message;

import BookLink.BookLink.Domain.Message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


}
