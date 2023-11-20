package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.MessageDto;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;

    // 처음 메세지를 보내는
    @PostMapping("/start")
    public ResponseEntity<ResponseDto> startMessage(@RequestBody MessageStartDto.Request request,
                                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = messageService.startMessage(request, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 이미 시작된 쪽지를 보낼 때
    @PostMapping("/send")
    public ResponseEntity<ResponseDto> sendMessage(@RequestBody MessageDto.Request request,
                                                   @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = messageService.sendMessage(request, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 사용자의 쪽지 리스트 반환하기
    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getMessageList(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = messageService.messageRoomList(member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 쪽지방 입장하기 (쪽지들 뿌려주기)
    @GetMapping("/entrance/{room_id}")
    public ResponseEntity<ResponseDto> entranceMessageRoom(@PathVariable Long room_id) {

        ResponseDto responseDto = messageService.entranceMessageRoom(room_id);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }


    //    // 메세지 삭제하기
    @DeleteMapping("/{message_id}")
    public ResponseEntity<ResponseDto> deleteMessage(@PathVariable Long message_id, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Member member = memberPrincipal.getMember();
        ResponseDto responseDto = messageService.deleteMessage(message_id, member);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }
}
