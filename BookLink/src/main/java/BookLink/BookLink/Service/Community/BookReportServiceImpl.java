package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReport;
import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReportServiceImpl implements BookReportService{

    private final BookReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseDto writeReport(BookReportDto.Request requestDto, String memEmail) {

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        BookReport bookReport = BookReportDto.Request.toEntity(requestDto, loginMember);
        reportRepository.save(bookReport);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("정상 등록 완료");

        return responseDto;

    }
}
