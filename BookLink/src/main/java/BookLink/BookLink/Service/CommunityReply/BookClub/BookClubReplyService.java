package BookLink.BookLink.Service.CommunityReply.BookClub;

import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;

import java.net.MalformedURLException;

public interface BookClubReplyService {

    ResponseDto writeReply(Member member, Long postId, BookClubReplyDto.Request replyDto);

    ResponseDto updateReply(Long postId, Long replyId, BookClubReplyUpdateDto replyDto);

    ResponseDto deleteReply(Long postId, Long replyId);

    ResponseDto likeReply(Member member, Long postId, Long replyId);

}
