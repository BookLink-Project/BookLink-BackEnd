package BookLink.BookLink.Domain.Chat;

import BookLink.BookLink.Domain.Member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private Member sender;

    private String message;

    @Builder
    public ChatMessage(ChatRoom chatRoom, Member sender, String message) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
    }
}
