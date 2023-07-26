package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookReportReplyService {

    ResponseDto writeReply(String memEmail, Long postId, BookReportReplyDto.Request replyDto);
}