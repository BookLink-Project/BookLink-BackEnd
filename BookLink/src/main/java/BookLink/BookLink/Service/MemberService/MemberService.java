package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.TokenDto;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    ResponseDto joinMember(MemberDto.Request memberDTO);

    ResponseDto emailDoubleCheck(String email);

    ResponseDto nicknameDoubleCheck(String nickname);

    ResponseDto loginJwt(LoginDto.Request loginDto, HttpServletResponse response) throws Exception;
}
