package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.BookReport.BookReportDto;
import BookLink.BookLink.Domain.Community.BookReport.BookReportLike;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookReportLikeRepository;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookReportServiceImpl implements BookReportService{

    private final BookReportRepository bookReportRepository;
    private final MemberRepository memberRepository;
    private final BookReportLikeRepository bookReportLikeRepository;

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
    }

    @Override
    public ResponseDto reportDetail(Long id) {
        Optional<BookReport> byId = bookReportRepository.findById(id);

        ResponseDto responseDto = new ResponseDto();

        if (byId.isEmpty()) {
            // 에러처리
        }

        BookReport bookReport = byId.get();
        bookReport.view_plus();
        BookReportDto.Response response = BookReportDto.Response.toDto(bookReport);

//        responseDto.setStatus(HttpStatus.OK);
//        responseDto.setMessage("올바른 접근입니다.");
        responseDto.setData(response);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto reportUpdate(Long id, BookReportDto.Request requestDto) {
        Optional<BookReport> byId = bookReportRepository.findById(id);

        if (byId.isEmpty()) {
            //예외처리
        }

        BookReport bookReport = byId.get();
        bookReport.update(requestDto.getTitle(), requestDto.getContent());
//        bookReportRepository.save(bookReport);

        ResponseDto responseDto = new ResponseDto();

        return responseDto;
    }

    @Override
    public ResponseDto likePost(Long id, String memEmail) {
        ResponseDto responseDto = new ResponseDto();

        Optional<Member> byEmail = memberRepository.findByEmail(memEmail);
        Member member = byEmail.get();

        Optional<BookReport> byId = bookReportRepository.findById(id);
        BookReport bookReport = byId.get();
        Long reportId = bookReport.getId();

        boolean exists = bookReportLikeRepository.existsByMemberAndPost(member, bookReport);

        if (exists) {
            bookReportLikeRepository.deleteById(reportId);
            bookReport.like_minus();

            responseDto.setMessage("좋아요 취소");
        } else {
            BookReportLike bookReportLike = BookReportLike.builder()
                    .post(bookReport)
                    .member(member)
                    .build();

            bookReportLikeRepository.save(bookReportLike);
            bookReport.like_plus();

            responseDto.setMessage("좋아요 성공");
        }

        return responseDto;


    }



}

