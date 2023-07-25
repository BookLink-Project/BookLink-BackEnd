package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

public class BookReportDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String book_title;
        private String authors;
        private String publisher;
        private LocalDate pud_date;
        // 표지이미지
        private String title;
        private String content;

        public static BookReport toEntity(BookReportDto.Request request, Member writer) {
            return BookReport.builder()
                    .book_title(request.getBook_title())
                    .authors(request.getAuthors())
                    .publisher(request.getPublisher())
                    .pud_date(request.getPud_date())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .writer(writer)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String book_title;
        private String authors;
        private String publisher;
        private LocalDate pud_date;
        // 표지이미지
        private String title;
        private String content;
        private String writer;

        private Long like_cnt;
        private Long view_cnt;
        private Long reply_cnt;

        @Builder
        public Response(Long id, String book_title, String authors, String publisher, LocalDate pud_date, String title, String content,
                        String writer, Long like_cnt, Long view_cnt, Long reply_cnt) {
            this.id = id;
            this.book_title = book_title;
            this.authors = authors;
            this.publisher = publisher;
            this.pud_date = pud_date;
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.like_cnt = like_cnt;
            this.view_cnt = view_cnt;
            this.reply_cnt = reply_cnt;
        }

        public static BookReportDto.Response toDto(BookReport bookReport) {
            return Response.builder()
                    .id(bookReport.getId())
                    .book_title(bookReport.getBook_title())
                    .authors(bookReport.getAuthors())
                    .publisher(bookReport.getPublisher())
                    .pud_date(bookReport.getPud_date())
                    .title(bookReport.getTitle())
                    .content(bookReport.getContent())
                    .writer(bookReport.getWriter().getNickname())
                    .like_cnt(bookReport.getLike_cnt())
                    .view_cnt(bookReport.getView_cnt())
                    .reply_cnt(bookReport.getReply_cnt())
                    .build();
        }
    }
}
