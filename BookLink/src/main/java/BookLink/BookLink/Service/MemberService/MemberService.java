package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.MemberDto;

public interface MemberService {

    void joinMember(MemberDto.Request memberDTO);

    boolean emailDoubleCheck(String email);
}
