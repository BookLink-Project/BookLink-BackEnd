package BookLink.BookLink.Controller.Chat;

import BookLink.BookLink.Domain.Chat.ChatMessageDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private SimpMessageSendingOperations sendingOperations;
    private ChatService chatService;

    @MessageMapping("/chat/message")
    public ResponseEntity<ResponseDto> enter(ChatMessageDto.Request message, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }

        Member sender = memberPrincipal.getMember();

        ResponseDto responseDto = chatService.sendMessage(message, sender);

        sendingOperations.convertAndSend("/topic/chat/room/"+ message.getRoom_id(),message);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}