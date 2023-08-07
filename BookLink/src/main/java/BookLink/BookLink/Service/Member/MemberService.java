package BookLink.BookLink.Service.Member;

import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    ResponseDto joinMember(MemberDto.Request memberDto);

    ResponseDto emailDoubleCheck(String email);

    ResponseDto nicknameDoubleCheck(String nickname);

    ResponseDto loginJwt(LoginDto.Request loginDto, HttpServletResponse response);

    ResponseDto logoutJwt(HttpServletResponse response, MemberPrincipal memberPrincipal);

}
