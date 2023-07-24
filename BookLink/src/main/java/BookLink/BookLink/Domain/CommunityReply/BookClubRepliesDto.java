package BookLink.BookLink.Domain.CommunityReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookClubRepliesDto {

    private Long id;
    private Long parent_id;
    private String writer;
    private String content;
    private LocalDateTime date;
    private URL image;
    private Long like_cnt;
    private Long sub_reply_cnt;

    private Boolean isLiked;

    private Boolean isUpdated;

}
