package BookLink.BookLink.Domain.Message;

import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Member.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MessageRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "room_id")
    private List<Message> messages = new ArrayList<>();

    @ManyToOne // 대여 신청하는 사람
    @JoinColumn(name = "sender")
    private Member sender;

    @ManyToOne // 대여 해주는 사람
    @JoinColumn(name = "receiver")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "rent_id")
    private BookRent bookRent;


    public MessageRoom(Member sender, Member receiver, BookRent bookRent) {
        this.sender = sender;
        this.receiver = receiver;
        this.bookRent = bookRent;
    }

    public MessageRoom() {

    }
}
