package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Book.BookDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MyPageService {

    ResponseDto showHistory(Member member, String rentType, String communityType);

    ResponseDto showAccount(Member member);

    ResponseDto verifyAccount(VerifyDto verifyDto, Member member);

    ResponseDto updateAccount(MultipartFile image, AccountDto.Request accountDto, Member member) throws IOException;

    ResponseDto myBook(Member member);

    ResponseDto myRentBook(Integer page, Member member);

    ResponseDto myLendBook(Integer page, Member member);

    ResponseDto openRentBook(BookDto.Request bookDto, Long book_id, Member member, List<MultipartFile> image) throws IOException;

    ResponseDto blockRentBook(Long book_id, Member loginMember);
}
