package BookLink.BookLink.Domain.Community;

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

        private String title;
        private String content;
        private String location;
        private String writer;
        private LocalDateTime date;

        private Long reply_cnt;

        @Builder
        public Response(String title, String content, String location, String writer, LocalDateTime date, Long reply_cnt) {
            this.title = title;
            this.content = content;
            this.location = location;
            this.writer = writer;
            this.date = date;
            this.reply_cnt = reply_cnt;
        }
    }

}
