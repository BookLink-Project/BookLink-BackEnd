package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void joinMember(MemberDto.Request memberDTO) {
        Member member = MemberDto.Request.toEntity(memberDTO);
        memberRepository.save(member);
    }

    @Override
    public boolean emailDoubleCheck(String email) {
        boolean is_exist  = memberRepository.existsByEmail(email);
        return is_exist;
    }
}
