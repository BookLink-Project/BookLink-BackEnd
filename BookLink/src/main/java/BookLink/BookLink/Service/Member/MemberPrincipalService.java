package BookLink.BookLink.Service.Member;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPrincipalService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override // DB에 존재하는지
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 우린 이메일로 로그인 하니까.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("해당 계정을 찾을 수 없음 : " + email));

        return new MemberPrincipal(member);
    }
}
