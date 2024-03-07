package BookLink.BookLink.Domain.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RentDto {

    private LocalDateTime rent_date;
    private LocalDateTime return_date;
    private String return_location;
    private String rent_nickname;
}
