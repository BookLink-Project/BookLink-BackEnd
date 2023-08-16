package BookLink.BookLink.Domain.Community.FreeBoard;

import BookLink.BookLink.Domain.Member.Member;
import lombok.*;

import java.time.LocalDateTime;


public class FreeBoardDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String category;
        private String title;
        private String content;

        public FreeBoard toEntity(Member member) {
            return FreeBoard.builder()
                    .writer(member)
                    .category(category)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String category;
        private String title;
        private String content;
        private String writer;

        private Long like_cnt;
        private Long view_cnt;
        private Long reply_cnt;

        private LocalDateTime date;

        @Builder
        public Response(Long id, String category, String title, String content, String writer, Long like_cnt,
                        Long view_cnt, Long reply_cnt, LocalDateTime date) {
            this.id = id;
            this.category = category;
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.like_cnt = like_cnt;
            this.view_cnt = view_cnt;
            this.reply_cnt = reply_cnt;
            this.date = date;
        }

        public static FreeBoardDto.Response toDto(FreeBoard freeBoard) {
            return Response.builder()
                    .id(freeBoard.getId())
                    .category(freeBoard.getCategory())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .writer(freeBoard.getWriter().getNickname())
                    .like_cnt(freeBoard.getLike_cnt())
                    .view_cnt(freeBoard.getView_cnt())
                    .reply_cnt(freeBoard.getReply_cnt())
                    .date(freeBoard.getCreatedTime())
                    .build();
        }
    }


}
