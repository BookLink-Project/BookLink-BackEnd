package BookLink.BookLink.Service.Message;

import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.MessageDto;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface MessageService {

    ResponseDto startMessage(MessageStartDto.Request messageDto, MemberPrincipal memberPrincipal);

    ResponseDto sendMessage(MessageDto.Request messageDto, MemberPrincipal memberPrincipal);
}
