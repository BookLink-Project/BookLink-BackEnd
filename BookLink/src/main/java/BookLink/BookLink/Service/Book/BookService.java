package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Book.RentDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface BookService {

    ResponseDto callApi(String query);

    ResponseDto joinMyBook(BookDto.Request bookDto, Member loginMember, List<MultipartFile> image) throws IOException;

    ResponseDto listAllBook(Integer category, int page);

    ResponseDto searchBook(Integer category, String searchWord, int page);

    ResponseDto showBook(MemberPrincipal memberPrincipal, String isbn13);

    ResponseDto likeBook(Member member, String isbn);

    ResponseDto rentBookList(Integer page);

    ResponseDto rentBookDescList(Integer page);

    ResponseDto rentBookCategoryList(String category, Integer page);

    ResponseDto rentBookCategoryDescList(String category, Integer page);

    ResponseDto rentBookSearch(String title);

    ResponseDto rentBooks(String title);

    ResponseDto rentBookDetail(Long id);

    ResponseDto rentSuccess(Long id, RentDto rentDto, Member lender);

    ResponseDto searchHeaderBook(String search, Integer page);

}
