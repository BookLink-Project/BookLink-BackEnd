package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Getter;

@Getter
public class BookClubDto {

    private String title;
    private String content;
    private String location;

    public BookClub toEntity(Member member) {
        return BookClub.builder()
                .title(title)
                .content(content)
                .writer(member)
                .location(location)
                .build();
    }
}
