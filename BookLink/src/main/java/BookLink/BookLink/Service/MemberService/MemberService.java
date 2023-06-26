package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.Token.TokenDto;

public interface MemberService {

    void joinMember(MemberDto.Request memberDTO);

    boolean emailDoubleCheck(String email);

    TokenDto loginJwt(String email, String password) throws Exception;
}
