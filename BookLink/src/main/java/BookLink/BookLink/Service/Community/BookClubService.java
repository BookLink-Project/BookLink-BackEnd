package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClub.BookClubDto;
import BookLink.BookLink.Domain.Community.BookClub.BookClubUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubService {

    ResponseDto writePost(String memEmail, BookClubDto.Request bookClubDto);

    ResponseDto listPost();

    ResponseDto showPost(String memEmail, Long id) throws MalformedURLException;

    ResponseDto modifyPost(Long id, BookClubUpdateDto bookClubDto);

    ResponseDto deletePost(Long id);

}