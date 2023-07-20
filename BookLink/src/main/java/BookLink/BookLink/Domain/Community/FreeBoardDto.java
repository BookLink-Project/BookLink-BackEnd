package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


public class FreeBoardDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String title;
        private String content;

        public FreeBoard toEntity(Member member) {
            return FreeBoard.builder()
                    .writer(member)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Slf4j
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String title;
        private String content;
    }

}
