package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookReportDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookReportService {

    ResponseDto writeReport(BookReportDto.Request requestDto, String memEmail);
}
