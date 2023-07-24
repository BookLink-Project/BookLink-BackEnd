package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookClubReplyDto;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubReplyService {

    ResponseDto writeReply(String memEmail, Long id, BookClubReplyDto.Request replyDto) throws MalformedURLException;

}
