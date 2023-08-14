package BookLink.BookLink.Service.Member;

import BookLink.BookLink.Domain.Member.LoginDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberDto;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.Token.RefreshToken;
import BookLink.BookLink.Domain.Token.TokenDto;
import BookLink.BookLink.Exception.Enum.CommonErrorCode;
import BookLink.BookLink.Exception.Enum.MemberErrorCode;
import BookLink.BookLink.Exception.RestApiException;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

        ResponseDto responseDto = new ResponseDto();

        // 예외처리
        Optional<Member> member_email = memberRepository.findByEmail(memberDto.getEmail());
        Optional<Member> member_nickname = memberRepository.findByNickname(memberDto.getNickname());

        if(member_email.isPresent() || member_nickname.isPresent()){
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        }

        memberDto.setEncodePwd(passwordEncoder.encode(memberDto.getPassword())); // PW 암호화
        Member member = MemberDto.Request.toEntity(memberDto);
        memberRepository.save(member);

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("DB 저장 완료");
        return responseDto;
    }

    @Override
    public ResponseDto emailDoubleCheck(String email) {
        boolean is_exist  = memberRepository.existsByEmail(email);

        if(is_exist) {
            throw new RestApiException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("사용가능한 이메일입니다.");
        return responseDto;
    }

    @Override
    public ResponseDto nicknameDoubleCheck(String nickname) {
        boolean is_exist = memberRepository.existsByNickname(nickname);

        if(is_exist) {
            throw new RestApiException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("중복되지않는 닉네임");
        return responseDto;
    }

    @Override
    public ResponseDto loginJwt(LoginDto.Request loginDto, HttpServletResponse response) {

        ResponseDto responseDto = new ResponseDto();

        Optional<Member> selectedMember = memberRepository.findByEmail(loginDto.getEmail());

        if (selectedMember.isEmpty()) {
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setMessage("없는 이메일");
            return responseDto;
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), selectedMember.get().getPassword())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 비밀번호");
            return responseDto;
        }

        // 성공 시 access, refresh 토큰 생성
        TokenDto newTokenDto = jwtUtil.createAllToken(loginDto.getEmail());

        // Refresh Token 존재 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberEmail(loginDto.getEmail());

        if (refreshToken.isPresent()) { // 존재 -> 토큰 업데이트
            refreshTokenRepository.save(refreshToken.get().updateToken(newTokenDto.getRefreshToken()));
        } else { // 부재 -> 토큰 생성 후 DB 저장
            refreshTokenRepository.save(new RefreshToken(newTokenDto.getRefreshToken(), loginDto.getEmail()));
        }

        jwtUtil.setCookieAccessToken(response, newTokenDto.getAccessToken());
        jwtUtil.setCookieRefreshToken(response, newTokenDto.getRefreshToken());

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("로그인 성공");

        Member loginMember = selectedMember.get();
        LoginDto.Response loginData = new LoginDto.Response(
                loginMember.getEmail(), loginMember.getName(), loginMember.getNickname(), loginMember.getAddress()
        );

        responseDto.setData(loginData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto logoutJwt(HttpServletResponse response, MemberPrincipal memberPrincipal) {

        ResponseDto responseDto = new ResponseDto();

        if (memberPrincipal == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그아웃 대상이 아님");
            return responseDto;
        }

        SecurityContextHolder.clearContext();

        jwtUtil.removeTokenCookie(response);

        refreshTokenRepository.deleteByMemberEmail(memberPrincipal.getMember().getEmail());

        responseDto.setMessage("로그아웃 성공");

        return responseDto;
    }
}
