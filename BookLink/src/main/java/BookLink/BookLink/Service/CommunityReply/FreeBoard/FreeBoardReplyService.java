package BookLink.BookLink.Service.CommunityReply.FreeBoard;

import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReplyUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface FreeBoardReplyService {

    ResponseDto writeReply(String memEmail, Long postId, FreeBoardReplyDto.Request replyDto);

    ResponseDto updateReply(Long postId, Long replyId, FreeBoardReplyUpdateDto replyDto);

    ResponseDto deleteReply(Long postId, Long replyId);

    ResponseDto likeReply(String memEmail, Long postId, Long replyId);

}
