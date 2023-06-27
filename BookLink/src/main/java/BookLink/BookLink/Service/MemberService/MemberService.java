package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.TokenDto;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    void joinMember(MemberDto.Request memberDTO);

    boolean emailDoubleCheck(String email);

    ResponseDto loginJwt(LoginDto.Request loginDto, HttpServletResponse response) throws Exception;
}
