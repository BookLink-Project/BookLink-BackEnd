package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
                    .contents(request.getContent())
                    .writer(writer)
                    .build();
        }
    }
}
