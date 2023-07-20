package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.FreeBoardDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface FreeBoardService {

    ResponseDto writePost(String memEmail, FreeBoardDto.Request freeBoardDto);

    ResponseDto freeBoardList();
}
