package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface MyPageService {

    ResponseDto showAccount(Member member);

    ResponseDto verifyAccount(VerifyDto verifyDto, Member member);

    ResponseDto updateAccount(AccountDto.Request accountDto, Member member);

    ResponseDto myBook(Member member);

    ResponseDto myRentBook(Integer page, Member member);

    ResponseDto myLendBook(Integer page, Member member);

}
