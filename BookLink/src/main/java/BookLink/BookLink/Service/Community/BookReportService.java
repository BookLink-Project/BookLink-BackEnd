package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.ResponseDto;


public interface BookReportService {

    ResponseDto writeReport(BookReportDto.Request requestDto, String memEmail);

    ResponseDto reportList();

    ResponseDto reportDetail(Long id);

    ResponseDto reportUpdate(Long id, BookReportDto.Request requestDto);

    ResponseDto likePost(Long id, String memEmail);
}
