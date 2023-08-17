package BookLink.BookLink.Controller.Chat;

import BookLink.BookLink.Domain.Chat.ChatMessageDto;
import BookLink.BookLink.Domain.Chat.ChatRoom;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chat")
public class ChatRoomController {

    private final ChatService chatService;
    private SimpMessageSendingOperations sendingOperations;

//    @GetMapping("/room")
//    public String rooms(Model model) { // 그냥 화면
//        return "/chat/room";
//    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseDto> room() { // 채팅방 목록

        ResponseDto responseDto = chatService.findAllRoom();

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @PostMapping("/room/{book_id}")
    public ResponseEntity<ResponseDto> createRoom(@PathVariable Long book_id,
                                                  @AuthenticationPrincipal MemberPrincipal memberPrincipal) { // 채팅방 생성

        Member renter = memberPrincipal.getMember();
        ResponseDto responseDto = chatService.createRoom(book_id, renter);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) { // 채팅방 입장 화면
//
//        model.addAttribute("roomId", roomId);
//
//        return "/chat/roomdetail";
//    }

    @GetMapping("/room/{room_id}")
    public ResponseEntity<ResponseDto> roomInfo(@PathVariable Long room_id) { // 채팅방에 입장했을때 채팅 기록 뿌려주기

        ResponseDto responseDto = chatService.findChat(room_id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    @MessageMapping("/message")
    public ResponseEntity<ResponseDto> enter(@RequestBody ChatMessageDto.Request message, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
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