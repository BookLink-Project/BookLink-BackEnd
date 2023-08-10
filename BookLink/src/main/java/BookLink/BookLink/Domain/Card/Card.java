package BookLink.BookLink.Domain.Card;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String bank;

    @NotNull
    private String number; // 카드번호

    @NotNull
    private int cvc; // cvc 번호

    @NotNull
    private Date valid_date; // 유효기간

    @NotNull
    private String name; // 카드 이름

}
