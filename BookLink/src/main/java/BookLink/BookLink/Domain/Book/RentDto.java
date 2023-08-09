package BookLink.BookLink.Domain.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentDto {

    private Integer period;
    private String nickname;
}
