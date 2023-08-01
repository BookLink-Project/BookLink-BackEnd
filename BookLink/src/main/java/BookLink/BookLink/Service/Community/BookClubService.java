package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClub.BookClubDto;
import BookLink.BookLink.Domain.Community.BookClub.BookClubUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubService {

    ResponseDto writePost(Member member, BookClubDto.Request bookClubDto);

    ResponseDto listPost();

    ResponseDto showPost(Member member, Long id);

    ResponseDto modifyPost(Long id, BookClubUpdateDto bookClubDto);

    ResponseDto deletePost(Long id);

    ResponseDto likePost(Member member, Long id);

}