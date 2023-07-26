package BookLink.BookLink.Domain.CommunityReply.BookClubReply;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookClubReplyDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String content;
        private Long parentId;

        public BookClubReply toEntity(BookClub bookClub, Member member, BookClubReply parent) {

            return BookClubReply.builder()
                    .post(bookClub)
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