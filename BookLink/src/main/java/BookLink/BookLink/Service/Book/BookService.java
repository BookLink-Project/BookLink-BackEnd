package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookService {

    ResponseDto callApi(String query);

    ResponseDto joinMyBook(BookDto.Request bookDto, Member loginMember);

    ResponseDto listAllBook(Integer category);

    ResponseDto searchBook(Integer category, String searchWord);

    ResponseDto showBook(MemberPrincipal memberPrincipal, String isbn13);

    ResponseDto likeBook(Member member, String isbn);

    ResponseDto rentBookList();

    ResponseDto rentBookDescList();

    ResponseDto rentBookCategoryList(String category);

    ResponseDto rentBookCategoryDescList(String category);

    ResponseDto rentBookSearch(String title);

    ResponseDto rentBooks(String title);

}
