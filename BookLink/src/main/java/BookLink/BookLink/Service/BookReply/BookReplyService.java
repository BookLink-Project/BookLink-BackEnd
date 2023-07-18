package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;

public interface BookReplyService {

    ResponseDto writeReply(String memEmail, String isbn, BookReplyDto.Request replyDto);

    ResponseDto likeReply(String memEmail, Long replyId, String state);

}
