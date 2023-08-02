package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface FreeBoardService {

    ResponseDto writePost(String memEmail, FreeBoardDto.Request freeBoardDto);

    ResponseDto freeBoardList();

    ResponseDto freeBoardDetail(Long id, String memEmail);

    ResponseDto freeBoardUpdate(Long id, FreeBoardUpdateDto requestDto);

    ResponseDto likePost(Long id, String memEmail);

    ResponseDto deletePost(Long id);
}
