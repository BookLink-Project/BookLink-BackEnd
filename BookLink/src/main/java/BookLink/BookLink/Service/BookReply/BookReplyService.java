package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.BookReply.BookReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;

import java.net.MalformedURLException;

public interface BookReplyService {

    ResponseDto writeReply(Member member, String isbn, BookReplyDto.Request replyDto);

    ResponseDto likeReply(Member member, Long replyId, String isbn);

    ResponseDto updateReply(String isbn, Long replyId, BookReplyUpdateDto replyDto);

    ResponseDto deleteReply(String isbn, Long replyId);
}
