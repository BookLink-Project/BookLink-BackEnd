package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.*;
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String title;
        private String content;

        @Builder
        public Response(Long id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public static FreeBoardDto.Response toDto(FreeBoard freeBoard) {
            return Response.builder()
                    .id(freeBoard.getId())
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .build();
        }

    }


}
