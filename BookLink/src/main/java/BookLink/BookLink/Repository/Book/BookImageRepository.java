package BookLink.BookLink.Repository.Book;

import BookLink.BookLink.Domain.Book.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookImageRepository extends JpaRepository<BookImage, Integer> {

}
