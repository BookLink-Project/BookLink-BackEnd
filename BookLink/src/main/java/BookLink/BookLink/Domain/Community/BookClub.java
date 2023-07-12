package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class BookClub extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    @NotNull
    private String location;

    @Builder
    public BookClub(String title, String content, Member writer, String location) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.location = location;
    }
}
