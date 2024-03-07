package BookLink.BookLink.Domain.CommunityReply.BookReportReply;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

public class BookReportReplyDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private String content;
        private Long parentId;

        public BookReportReply toEntity(BookReport bookReport, Member member, BookReportReply parent) {

            return BookReportReply.builder()
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
