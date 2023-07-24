package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.CommunityReply.BookClubRepliesDto;
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
    private URL writer_image;

    private Long view_cnt;
    private Long like_cnt;
    private Long reply_cnt;

    private boolean isLiked;

    private List<BookClubRepliesDto> replies = new ArrayList<>();

}