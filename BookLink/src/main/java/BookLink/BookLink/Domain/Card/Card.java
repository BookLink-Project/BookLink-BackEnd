package BookLink.BookLink.Domain.Card;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long card_id;

    private String bank;

    private String number; // 카드번호

    private int cvc; // cvc 번호

    private Date valid_date; // 유효기간

}
