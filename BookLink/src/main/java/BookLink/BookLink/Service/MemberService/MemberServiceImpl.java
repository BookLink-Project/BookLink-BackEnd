package BookLink.BookLink.Service.MemberService;

import BookLink.BookLink.Domain.Common.StatusEnum;
import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import BookLink.BookLink.utils.JwtUtil;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseDto joinMember(MemberDto.Request memberDto) {

        // 예외처리
        Optional<Member> member_email = memberRepository.findByEmail(memberDto.getEmail());
        Optional<Member> member_nickname = memberRepository.findByNickname(memberDto.getNickname());

        ResponseDto responseDto = new ResponseDto();

        if(member_email.isPresent() || member_nickname.isPresent()){
            responseDto.setMessage("이미 이메일 존재");
            return responseDto;
        }

        memberDto.setEncodePwd(passwordEncoder.encode(memberDto.getPassword())); // PW 암호화

        Member member = MemberDto.Request.toEntity(memberDto);

        memberRepository.save(member);
        responseDto.setMessage("DB 저장 완료");
        return responseDto;
    }

    @Override
    public ResponseDto emailDoubleCheck(String email) {
        boolean is_exist  = memberRepository.existsByEmail(email);

        ResponseDto responseDto = new ResponseDto();
        if (is_exist) {
            responseDto.setStatus(StatusEnum.BAD_REQUEST);
            responseDto.setMessage("이미 이메일 존재");
        }
        else {
            responseDto.setStatus(StatusEnum.OK);
            responseDto.setMessage("중복되지않는 이메일");
        }
        return responseDto;
    }

    @Override
    public ResponseDto nicknameDoubleCheck(String nickname) {
        boolean is_exist = memberRepository.existsByNickname(nickname);

        ResponseDto responseDto = new ResponseDto();
        if (is_exist) {
            responseDto.setStatus(StatusEnum.BAD_REQUEST);
            responseDto.setMessage("이미 닉네임 존재");
        }
        else {
            responseDto.setStatus(StatusEnum.OK);
            responseDto.setMessage("중복되지않는 닉네임");
        }
        return responseDto;
    }

    @Override
    public ResponseDto loginJwt(LoginDto.Request loginDto, HttpServletResponse response) throws Exception {

        Optional<Member> selectedMember = memberRepository.findByEmail(loginDto.getEmail());

        ResponseDto responseDto = new ResponseDto();
        // 없는 email exception
        if (selectedMember.isEmpty()) {
            //  Member selectedMember = memberRepository.findByEmail(loginDto.getEmail())
            //          .orElseThrow(() -> new Exception(" 해당 계정이 존재하지 않습니다. - " + loginDto.getEmail()));
            //return new ResponseDto("invalid email", HttpStatus.UNAUTHORIZED.value());
            responseDto.setStatus(StatusEnum.UNAUTHORIZED);
            responseDto.setMessage("유효하지 않은 이메일");
        }

        // 잘못된 password exception
        if (!passwordEncoder.matches(loginDto.getPassword(), selectedMember.get().getPassword())) {
            // throw new Exception("잘못된 비밀번호입니다.");
            //return new ResponseDto("wrong password", HttpStatus.UNAUTHORIZED.value());
            responseDto.setStatus(StatusEnum.UNAUTHORIZED);
            responseDto.setMessage("유효하지 않은 비밀번호");
        }

        // Exception 미발생(성공) 시 토큰 생성
        TokenDto newTokenDto = jwtUtil.createAllToken(loginDto.getEmail()); // access, refresh 둘 다

        // Refresh Token 존재 유무 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(loginDto.getEmail());

        if (refreshToken.isPresent()) { // 존재 -> 토큰 업데이트
            refreshTokenRepository.save(refreshToken.get().updateToken(newTokenDto.getRefreshToken()));
        } else { // 부재 -> 토큰 생성 후 DB 저장
            refreshTokenRepository.save(
                    new RefreshToken(newTokenDto.getRefreshToken(), loginDto.getEmail())
            );
        }

        response.addHeader("Access_Token", newTokenDto.getAccessToken());
        response.addHeader("Refresh_Token", newTokenDto.getRefreshToken());

        return responseDto;
    }
}
