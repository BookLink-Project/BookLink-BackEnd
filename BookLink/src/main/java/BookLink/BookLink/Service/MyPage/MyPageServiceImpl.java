package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public ResponseDto verifyAccount(VerifyDto verifyDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (!passwordEncoder.matches(verifyDto.getPassword(), loginMember.getPassword())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 비밀번호");
            return responseDto;
        }
        return responseDto;
    }

    @Override
    public ResponseDto showAccount(Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        responseDto.setData(
                new AccountDto.Response(
                        loginMember.getImage(),
                        loginMember.getName(),
                        loginMember.getNickname(),
                        loginMember.getEmail(),
                        loginMember.getBirth(),
                        loginMember.getAddress()
                        // loginMember.getCard()
                )
        );
        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateAccount(AccountDto.Request accountDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        Member selectedMember = memberRepository.findById(loginMember.getId()).orElse(null);

        selectedMember.updateAccount(
                accountDto.getImage(),
                accountDto.getName(),
                accountDto.getNickname(),
                accountDto.getEmail(),
                passwordEncoder.encode(accountDto.getPassword()),
                accountDto.getBirth(),
                accountDto.getAddress()
//                accountDto.getCard()
        );

        responseDto.setStatus(HttpStatus.CREATED);
        return responseDto;
    }



    @Override
    public ResponseDto myRentBook(Integer page) {

        ResponseDto responseDto = new ResponseDto();

        return null;
    }
}
