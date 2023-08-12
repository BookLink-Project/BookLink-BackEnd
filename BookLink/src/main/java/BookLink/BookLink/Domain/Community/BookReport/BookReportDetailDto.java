package BookLink.BookLink.Domain.Community.BookReport;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportRepliesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookReportDetailDto {

    private String book_title;
    private String isbn;
    private String authors;
    private String publisher;
    private LocalDate pud_date;
    private String cover; // 도서 표지 미리보기 URL
    private String book_category; // 책 카테고리
    private String community_category; // 커뮤니티 카테고래
    private String title;
    private String content;

    private LocalDateTime date;
    private String Writer;
    private URL image;

    private Long view_cnt;
    private Long like_cnt;
    private Long reply_cnt;

    private boolean isLiked;
    private boolean isUpdated;

    private List<BookReportRepliesDto> replies = new ArrayList<>();

}
