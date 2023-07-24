package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.Community.FreeBoard;
import BookLink.BookLink.Domain.Community.FreeBoardDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.FreeBoardRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseDto writePost(String memEmail, FreeBoardDto.Request freeBoardDto) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        FreeBoard freeBoard = freeBoardDto.toEntity(loginMember);

        try {
            freeBoardRepository.save(freeBoard);

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
    public ResponseDto freeBoardList() {

        ResponseDto responseDto = new ResponseDto();

        List<FreeBoard> all_freeBoard = freeBoardRepository.findAll();
        List<FreeBoardDto.Response> free_response = new ArrayList<>();

        for (int i = 0; i < all_freeBoard.size(); i++) {
            FreeBoardDto.Response response = FreeBoardDto.Response.toDto(all_freeBoard.get(i));
            free_response.add(i,response);
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("자유글 목록 조회입니다.");
        responseDto.setData(free_response);

        return responseDto;

    }


}
