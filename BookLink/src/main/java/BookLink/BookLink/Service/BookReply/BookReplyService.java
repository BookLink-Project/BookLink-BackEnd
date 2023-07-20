package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;

import java.net.MalformedURLException;

public interface BookReplyService {

    ResponseDto writeReply(String memEmail, String isbn, BookReplyDto.Request replyDto) throws MalformedURLException;

    ResponseDto likeReply(String memEmail, Long replyId);

}
