package BookLink.BookLink.Domain.BookReply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookRepliesDto {

        private Long id;
        private Long parent_id; // 부모 댓글
        private String writer; // nickname
        private String content;
        private LocalDateTime date;
        private URL image;
        private Long like_cnt;
        private Long sub_reply_cnt;

        // 해당 계정이 좋아요 눌렀는지 안 눌렀는지
        private boolean isLiked;

}
