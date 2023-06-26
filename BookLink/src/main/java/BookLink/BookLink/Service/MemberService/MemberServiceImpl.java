package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60L; // 1 minute

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

    @Override
    public TokenDto loginJwt(String email, String password) throws Exception {

        // email 없음
        Member selectedMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception(" 해당 계정이 존재하지 않습니다. - " + email));

        // password 틀림
        if (!password.equals(selectedMember.getPassword())) {
            throw new Exception("잘못된 비밀번호입니다.");
        }

        // 앞에서 exception 미발생(성공) 시 토큰 발행
        String token = JwtUtil.createJwt(email, secretKey, expiredMs);

        return new TokenDto(token);
    }
}
