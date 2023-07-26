package BookLink.BookLink.Domain.Community.BookClub;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubRepliesDto;
import lombok.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookClubDetailDto {

    private String title;
    private String location;
    private String content;
    private LocalDateTime date;
    private String writer;
    private URL image;

    private Long view_cnt;
    private Long like_cnt;
    private Long reply_cnt;

    private boolean isLiked;

    private List<BookClubRepliesDto> replies = new ArrayList<>();

}
