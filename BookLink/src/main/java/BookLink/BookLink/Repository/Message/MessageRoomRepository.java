package BookLink.BookLink.Repository.Message;

import BookLink.BookLink.Domain.Message.MessageRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    // Member의 nickname으로 sender 또는 receiver가 일치하는 MessageRoom 찾기
    @Query("SELECT DISTINCT mr FROM MessageRoom mr " +
            "LEFT JOIN FETCH mr.sender s " +
            "LEFT JOIN FETCH mr.receiver r " +
            "WHERE s.nickname = :nickname OR r.nickname = :nickname")
    List<MessageRoom> findMessageRoomsByMemberNickname(@Param("nickname") String nickname);
}
