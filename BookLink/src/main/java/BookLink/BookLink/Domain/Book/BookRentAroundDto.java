package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRentAroundDto {

    private Long id;
    private String owner_nickname;
    private URL owner_image;
    private Integer max_date;
    private Integer fee;
    // private Float distance;

}
