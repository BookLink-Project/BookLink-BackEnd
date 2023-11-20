package BookLink.BookLink.Service.Message;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.MessageDto;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface MessageService {

    ResponseDto startMessage(MessageStartDto.Request messageDto, Member loginMember);

    ResponseDto sendMessage(MessageDto.Request messageDto, Member loginMember);

    ResponseDto messageRoomList(Member loginMember);
}
