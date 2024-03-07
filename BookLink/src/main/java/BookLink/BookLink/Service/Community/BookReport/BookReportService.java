package BookLink.BookLink.Service.Community.BookReport;

import BookLink.BookLink.Domain.Community.BookReport.BookReportDto;
import BookLink.BookLink.Domain.Community.BookReport.BookReportUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;


public interface BookReportService {

    ResponseDto writeReport(BookReportDto.Request requestDto, Member member);

    ResponseDto reportList();

    ResponseDto reportDetail(Long id, MemberPrincipal memberPrincipal);

    ResponseDto reportUpdate(Long id, BookReportUpdateDto requestDto);

    ResponseDto likePost(Long id, Member member);

    ResponseDto deletePost(Long id);
}
