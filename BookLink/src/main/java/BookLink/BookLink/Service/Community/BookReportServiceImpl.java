package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReport;
import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.Community.FreeBoard;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.Community.FreeBoardRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookReportServiceImpl implements BookReportService{

    private final BookReportRepository bookReportRepository;
    private final MemberRepository memberRepository;
    private final FreeBoardRepository freeBoardRepository;

    @Override
    public ResponseDto writeReport(BookReportDto.Request requestDto, String memEmail) {

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        BookReport bookReport = BookReportDto.Request.toEntity(requestDto, loginMember);
        bookReportRepository.save(bookReport);

        ResponseDto responseDto = new ResponseDto();

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("정상 등록 완료");

        return responseDto;

    }

    @Override
    public ResponseDto reportList() {

        ResponseDto responseDto = new ResponseDto();

        List<BookReport> all_report = bookReportRepository.findAll();
        List<BookReportDto.Response> report_response = new ArrayList<>();

        for (int i = 0; i < all_report.size(); i++) {
            BookReportDto.Response response = BookReportDto.Response.toDto(all_report.get(i));
            report_response.add(i,response);
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("독후감 목록 조회입니다.");
        responseDto.setData(report_response);

        return responseDto;

//        List<FreeBoard> all_board = freeBoardRepository.findAll();

//        List<Object> mergeList = new ArrayList<>();
//        mergeList.addAll(all_board);
//        mergeList.addAll(all_report);

//        // 만든 시간 순으로 정렬
//        Collections.sort(mergeList, new Comparator<Object>() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                LocalDateTime time1 = getCreationTime(o1);
//                LocalDateTime time2 = getCreationTime(o2);
//                return LocalDateTime.MAX.compareTo();
//            }
//
//            private LocalDateTime getCreationTime(Object entity) {
//                if (entity instanceof BookReport) {
//                    return ((BookReport) entity).getCreatedTime();
//                } else if (entity instanceof FreeBoard) {
//                    return ((FreeBoard) entity).getCreatedTime();
//                } else {
//                    throw new IllegalArgumentException("Unknown entity type");
//                }
//            }
//        });
    }


}
