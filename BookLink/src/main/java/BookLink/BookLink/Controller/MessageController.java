package BookLink.BookLink.Controller;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Service.Message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<ResponseDto> verifyAccount(@RequestBody MessageStartDto.Request request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {


        ResponseDto responseDto = messageService.startMessage(request, memberPrincipal);

        return ResponseEntity.status(responseDto.getStatus())
                .body(responseDto);
    }

    // 이미 시작된 쪽지를 보낼 때

    //
}
