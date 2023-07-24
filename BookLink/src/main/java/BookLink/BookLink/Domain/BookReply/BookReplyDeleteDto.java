package BookLink.BookLink.Domain.BookReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookReplyDeleteDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private Long replyId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private boolean isDeleted;

    }

}
