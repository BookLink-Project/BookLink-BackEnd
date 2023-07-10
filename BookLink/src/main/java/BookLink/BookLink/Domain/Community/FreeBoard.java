package BookLink.BookLink.Domain.Community;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class FreeBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;

    private String title;

    private String content;
}
