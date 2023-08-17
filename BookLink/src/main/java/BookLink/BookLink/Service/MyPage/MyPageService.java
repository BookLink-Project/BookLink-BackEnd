package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MyPageService {

    ResponseDto showHistory(Member member, String rentType, String communityType);

    ResponseDto showAccount(Member member);

    ResponseDto verifyAccount(VerifyDto verifyDto, Member member);

    ResponseDto updateAccount(MultipartFile image, AccountDto.Request accountDto, Member member) throws IOException;

    ResponseDto myBook(Member member);

    ResponseDto myRentBook(Integer page, Member member);

    ResponseDto myLendBook(Integer page, Member member);

    ResponseDto blockRentBook(Long book_id, Member loginMember);
}
