package BookLink.BookLink.Service.Community.BookReport;

import BookLink.BookLink.Domain.Community.BookReport.BookReportDto;
import BookLink.BookLink.Domain.Community.BookReport.BookReportUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;


public interface BookReportService {

    ResponseDto writeReport(BookReportDto.Request requestDto, String memEmail);

    ResponseDto reportList();

    ResponseDto reportDetail(Long id, String memEmail);

    ResponseDto reportUpdate(Long id, BookReportUpdateDto requestDto);

    ResponseDto likePost(Long id, String memEmail);

    ResponseDto deletePost(Long id);
}
