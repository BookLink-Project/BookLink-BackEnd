package BookLink.BookLink.Domain.Message;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "sender")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "recipient")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageRoom message_id;

    @Builder
    public Message(String contents, Member sender, Member receiver, MessageRoom message_id) {
        this.contents = contents;
        this.sender = sender;
        this.receiver = receiver;
        this.message_id = message_id;
    }
}
