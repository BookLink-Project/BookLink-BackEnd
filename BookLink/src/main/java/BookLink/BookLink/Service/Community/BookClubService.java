package BookLink.BookLink.Service.Community;

import BookLink.BookLink.Domain.Community.BookClubDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookClubService {

    ResponseDto writePost(String memEmail, BookClubDto bookClubDto);

}
