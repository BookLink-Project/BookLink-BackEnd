package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRelatedPostDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String writer;
    private URL image;

}
