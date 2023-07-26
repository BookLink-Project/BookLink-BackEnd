package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface FreeBoardService {

    ResponseDto writePost(String memEmail, FreeBoardDto.Request freeBoardDto);

    ResponseDto freeBoardList();

    ResponseDto freeBoardDetail(Long id);

    ResponseDto freeBoardUpdate(Long id, FreeBoardDto.Request requestDto);
}
