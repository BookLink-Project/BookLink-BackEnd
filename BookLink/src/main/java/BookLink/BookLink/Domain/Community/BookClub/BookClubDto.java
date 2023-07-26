package BookLink.BookLink.Domain.Community.BookClub;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class BookClubDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private String title;
        private String content;
        private String location;

        public BookClub toEntity(Member member) {
            return BookClub.builder()
                    .title(title)
                    .content(content)
                    .writer(member)
                    .location(location)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String title;
        private String content;
        private String location;
        private String writer;
        private LocalDateTime date;

        private Long reply_cnt;

        @Builder
        public Response(Long id, String title, String content, String location, String writer, LocalDateTime date, Long reply_cnt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.location = location;
            this.writer = writer;
            this.date = date;
            this.reply_cnt = reply_cnt;
        }
    }

}
