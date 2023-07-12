package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDto {

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
