package BookLink.BookLink.Domain.Card;

import com.sun.istack.NotNull;
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

    @NotNull
    private String bank;

    @NotNull
    private String number; // 카드번호

    @NotNull
    private int cvc; // cvc 번호

    @NotNull
    private Date valid_date; // 유효기간

}
