package BookLink.BookLink.Domain.BookReply;

import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class BookReplyDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String content;
        private Long parentId;

        public BookReply toEntity(Member member, String isbn, BookReply parent) {

            return BookReply.builder()
                    .isbn(isbn)
                    .writer(member)
                    .content(content)
                    .parent(parent)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long replyId;
        private LocalDateTime date;
        private String content;
        private String writer;
        private URL image;
    }
}
