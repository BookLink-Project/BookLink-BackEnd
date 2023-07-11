package BookLink.BookLink.Domain.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 대댓글은 한 번에 or 답글 더보기 클릭 시 반환?
public class ReviewsDto {

        private Long id;
        private String writer; // nickname
        private String content;
        private LocalDateTime date;
        private URL image;
        private Long like_cnt;
        private Long hate_cnt;
        private Long reply_cnt;

}
