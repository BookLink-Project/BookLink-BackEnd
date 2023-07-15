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
        private String writer; // nickname
        private String content;
        private LocalDateTime date;
        private URL image;
        private Long like_cnt;
        private Long sub_reply_cnt;

}
