package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class FreeBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Builder
    public FreeBoard(Member writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}
