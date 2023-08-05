package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookService {

    ResponseDto callApi(String query);

    ResponseDto joinMyBook(BookDto.Request bookDto);

    ResponseDto listAllBook(Integer category);

    ResponseDto searchBook(Integer category, String searchWord);

    ResponseDto showBook(MemberPrincipal memberPrincipal, String isbn13);

    ResponseDto likeBook(Member member, String isbn);

}
