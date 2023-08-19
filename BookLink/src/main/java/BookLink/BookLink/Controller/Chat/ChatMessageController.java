package BookLink.BookLink.Controller.Chat;

import BookLink.BookLink.Domain.Chat.ChatMessageDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/api/v1/chat/message")
    public ResponseEntity<ResponseDto> enter(@RequestBody ChatMessageDto.Request message,
                                             @AuthenticationPrincipal MemberPrincipal memberPrincipal) { // check

//        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
//            message.setMessage(message.getSender()+"님이 입장하였습니다.");
//        }

        log.info("===MessageMapping START===");

        Member sender = memberPrincipal.getMember();
        log.info("sender={}", sender.getNickname());
//
        ResponseDto responseDto = chatService.sendMessage(message, sender);
//        ResponseDto responseDto = chatService.sendMessage(message);

        simpMessagingTemplate.convertAndSend("/sub/api/v1/chat/room/"+ message.getRoom_id(), message);

        log.info("===MessageMapping END===");

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
