package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookService {

    ResponseDto callApi(String query);

    ResponseDto joinMyBook(BookDto.Request bookDto, Member loginMember);

    ResponseDto listAllBook(Integer category);

    ResponseDto searchBook(Integer category, String searchWord);

    ResponseDto showBook(Member member, String isbn13);

    ResponseDto likeBook(Member member, String isbn);

    ResponseDto rentBookList();

}
