package BookLink.BookLink.Domain.Book;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;

@Entity
@Getter
@NoArgsConstructor
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private URL image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_rent_id")
    private BookRent bookRent;

    @Builder
    public BookImage(URL image_url, BookRent bookRent) {
        this.image_url = image_url;
        this.bookRent = bookRent;
    }
}
