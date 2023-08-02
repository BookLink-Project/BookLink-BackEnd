package BookLink.BookLink.Domain.CommunityReply.FreeBoardReply;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

public class FreeBoardReplyDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private String content;
        private Long parentId;

        public FreeBoardReply toEntity(FreeBoard bookReport, Member member, FreeBoardReply parent) {

            return FreeBoardReply.builder()
                    .post(bookReport)
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
