package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookClubRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookClubServiceImpl implements BookClubService {

    private final BookClubRepository bookClubRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseDto writePost(String memEmail, BookClubDto bookClubDto) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        BookClub bookClub = bookClubDto.toEntity(loginMember);

        try {
            bookClubRepository.save(bookClub);
        } catch (Exception ex) {

            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("입력 미완료");

            return responseDto;
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");
        // responseDto.setData(bookClub);

        return responseDto;
    }
}
