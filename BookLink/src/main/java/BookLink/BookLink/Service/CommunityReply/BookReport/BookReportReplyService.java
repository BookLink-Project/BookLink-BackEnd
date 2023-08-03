package BookLink.BookLink.Service.CommunityReply.BookReport;

import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;

public interface BookReportReplyService {

    ResponseDto writeReply(Member member, Long postId, BookReportReplyDto.Request replyDto);

    ResponseDto updateReply(Long postId, Long replyId, BookReportReplyUpdateDto replyDto);

    ResponseDto deleteReply(Long postId, Long replyId);

    ResponseDto likeReply(Member member, Long postId, Long replyId);
}
