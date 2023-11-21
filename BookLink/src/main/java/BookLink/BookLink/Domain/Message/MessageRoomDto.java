package BookLink.BookLink.Domain.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRoomDto {

    private Long room_id;
    private String title;
    private String contents;
}
