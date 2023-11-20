package BookLink.BookLink.Domain.Message;

import BookLink.BookLink.Domain.Message.MessageDto.Response;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessagesDto {

    private List<Response> messages;
}
