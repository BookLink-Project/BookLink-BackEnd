package BookLink.BookLink.Domain.Community.FreeBoard;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportRepliesDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardRepliesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDetailDto {

    private String category;
    private String title;
    private String content;

    private LocalDateTime date;
    private String writer;
    private URL image;

    private Long view_cnt;
    private Long like_cnt;
    private Long reply_cnt;

    private boolean isLiked;
    private boolean isUpdated;

    private List<FreeBoardRepliesDto> replies = new ArrayList<>();
}
