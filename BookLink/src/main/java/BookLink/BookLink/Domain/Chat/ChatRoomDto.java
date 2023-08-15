package BookLink.BookLink.Domain.Chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    private Long room_id;
    private Long book_id;
    private String rent_nickname;
    private String lend_nickname;

    @Builder
    public ChatRoomDto(Long room_id, Long book_id, String rent_nickname, String lend_nickname) {
        this.room_id = room_id;
        this.book_id = book_id;
        this.rent_nickname = rent_nickname;
        this.lend_nickname = lend_nickname;
    }
}