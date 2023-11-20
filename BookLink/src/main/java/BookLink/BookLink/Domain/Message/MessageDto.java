package BookLink.BookLink.Domain.Message;

import BookLink.BookLink.Domain.Member.Member;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MessageDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String content;
        private String receiver;
        private Long room_id;

        public static Message toEntity(MessageDto.Request requestDto, Member sender, Member receiver, MessageRoom messageRoom) {
            return Message.builder()
                    .contents(requestDto.getContent())
                    .sender(sender)
                    .receiver(receiver)
                    .room_id(messageRoom)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long message_id;
        private String content;
        private String sender;
        private String receiver;
        private LocalDateTime created_time;
    }
}
