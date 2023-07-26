package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.FreeBoardRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public ResponseDto freeBoardDetail(Long id) {
        Optional<FreeBoard> byId = freeBoardRepository.findById(id);

        ResponseDto responseDto = new ResponseDto();

        if (byId.isEmpty()) {
            // 에러처리
        }

        FreeBoard freeBoard = byId.get();
        FreeBoardDto.Response response = FreeBoardDto.Response.toDto(freeBoard);


        responseDto.setData(response);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto freeBoardUpdate(Long id, FreeBoardDto.Request requestDto) {
        Optional<FreeBoard> byId = freeBoardRepository.findById(id);

        if (byId.isEmpty()) {
            //예외처리
        }


        FreeBoard freeBoard = byId.get();
        freeBoard.update(requestDto.getTitle(), requestDto.getContent());
//        bookReportRepository.save(bookReport);

        ResponseDto responseDto = new ResponseDto();

        return responseDto;

    }

}
