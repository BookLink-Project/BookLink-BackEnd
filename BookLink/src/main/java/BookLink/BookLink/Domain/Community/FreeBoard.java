package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    @JsonIgnore
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

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
