package BookLink.BookLink.Service.CommunityReply.BookClub;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyUpdateDto;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubReplyService {

    ResponseDto writeReply(String memEmail, Long postId, BookClubReplyDto.Request replyDto) throws MalformedURLException;

    ResponseDto updateReply(Long postId, Long replyId, BookClubReplyUpdateDto replyDto);

    ResponseDto deleteReply(Long postId, Long replyId);

    ResponseDto likeReply(String memEmail, Long postId, Long replyId);

}
