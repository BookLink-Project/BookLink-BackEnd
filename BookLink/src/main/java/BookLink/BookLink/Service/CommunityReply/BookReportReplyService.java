package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookReportReplyService {

    ResponseDto writeReply(String memEmail, Long postId, BookReportReplyDto.Request replyDto);

    ResponseDto updateReply(Long postId, Long replyId, BookReportReplyUpdateDto replyDto);

    ResponseDto deleteReply(Long postId, Long replyId);

    ResponseDto likeReply(String memEmail, Long postId, Long replyId);
}
