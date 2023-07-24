package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubService {

    ResponseDto writePost(String memEmail, BookClubDto.Request bookClubDto);

    ResponseDto listPost();

    ResponseDto showPost(String memEmail, Long id) throws MalformedURLException;
}
