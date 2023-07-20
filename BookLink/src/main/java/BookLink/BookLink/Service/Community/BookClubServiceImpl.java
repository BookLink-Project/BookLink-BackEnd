package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClub;
import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookClubRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookClubServiceImpl implements BookClubService {

    private final BookClubRepository bookClubRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseDto writePost(String memEmail, BookClubDto.Request bookClubDto) {

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

        return responseDto;
    }

    @Override
    public ResponseDto listPost() {

        ResponseDto responseDto = new ResponseDto();

        List<BookClub> bookClubList = bookClubRepository.findAll();
        List<BookClubDto.Response> responseData = new ArrayList<>();

        for (BookClub bookClub : bookClubList) {

            BookClubDto.Response response = BookClubDto.Response.builder()
                    .title(bookClub.getTitle())
                    .writer(bookClub.getWriter().getNickname())
                    .content(bookClub.getContent())
                    .location(bookClub.getLocation())
                    .date(bookClub.getCreatedTime())
                    .reply_cnt(10L) // TODO dummy
                    .build();

            responseData.add(response);

        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("목록 조회 성공");
        responseDto.setData(responseData);

        return responseDto;

    }
}
