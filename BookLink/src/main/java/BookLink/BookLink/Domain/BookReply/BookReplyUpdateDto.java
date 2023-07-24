package BookLink.BookLink.Domain.BookReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookReplyUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private Long replyId;
        private String content;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String content;
        private boolean isUpdated;

    }


}
