package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;


public interface BookService {

    ResponseDto callApi(String query);

    ResponseDto joinMyBook(BookDto.Request bookDto);

    ResponseDto listAllBook(Integer category);

    ResponseDto searchBook(Integer category, String searchWord);

    ResponseDto showBook(String memEmail, String isbn13);

    ResponseDto likeBook(String memEmail, String isbn);

}
