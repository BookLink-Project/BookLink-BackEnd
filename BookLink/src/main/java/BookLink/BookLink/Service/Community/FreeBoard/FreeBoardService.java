package BookLink.BookLink.Service.Community.FreeBoard;

import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardDto;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoardUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;

public interface FreeBoardService {

    ResponseDto writePost(Member member, FreeBoardDto.Request freeBoardDto);

    ResponseDto freeBoardList();

    ResponseDto freeBoardDetail(Long id, MemberPrincipal memberPrincipal);

    ResponseDto freeBoardUpdate(Long id, FreeBoardUpdateDto requestDto);

    ResponseDto likePost(Long id, Member member);

    ResponseDto deletePost(Long id);
}
