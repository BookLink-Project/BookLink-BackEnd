package BookLink.BookLink.Service.Message;

import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.Message;
import BookLink.BookLink.Domain.Message.MessageDto;
import BookLink.BookLink.Domain.Message.MessageRoom;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.Message.MessageStartDto.Request;
import BookLink.BookLink.Domain.Message.MessageStartDto.Response;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Message.MessageRepository;
import BookLink.BookLink.Repository.Message.MessageRoomRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MemberRepository memberRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final BookRentRepository bookRentRepository;

    @Override
    public ResponseDto startMessage(MessageStartDto.Request messageDto, MemberPrincipal memberPrincipal) {

        ResponseDto responseDto = new ResponseDto();

        if (memberPrincipal == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        Member sender = memberPrincipal.getMember();
        String receiver_nickname = messageDto.getReceiver();

        Member receiver = memberRepository.findByNickname(receiver_nickname).orElse(null);

        if (receiver == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("받는 사람의 정보가 불명확합니다.");
            return responseDto;
        }

        MessageRoom messageRoom = new MessageRoom();
        messageRoomRepository.save(messageRoom);

        Message message = Request.toEntity(messageDto, sender, receiver, messageRoom);
        messageRepository.save(message);

        Long rent_id = messageDto.getRent_id();
        BookRent bookRent = bookRentRepository.findById(rent_id).orElse(null);

        if(bookRent == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("대여 책을 다시 확인해주세요");
            return responseDto;
        }

        String book_title = bookRent.getBook().getTitle();
        Integer rent_date = messageDto.getRent_date();
        Integer rental_fee = bookRent.getRental_fee();

        Response response = Response.builder()
                .content(messageDto.getContent())
                .sender(sender.getNickname())
                .receiver(messageDto.getReceiver())
                .book_title(book_title)
                .rent_date(rent_date)
                .rental_fee(rental_fee)
                .created_time(message.getCreatedTime())
                .build();

        responseDto.setData(response);
        return responseDto;
    }

    @Override
    public ResponseDto sendMessage(MessageDto.Request messageDto, MemberPrincipal memberPrincipal) {

        ResponseDto responseDto = new ResponseDto();

        if (memberPrincipal == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        Member sender = memberPrincipal.getMember();
        String receiver_nickname = messageDto.getReceiver();

        Member receiver = memberRepository.findByNickname(receiver_nickname).orElse(null);

        if (receiver == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("받는 사람의 정보가 불명확합니다.");
            return responseDto;
        }

        Long message_id = messageDto.getMessage_id();
        MessageRoom messageRoom = messageRoomRepository.findById(message_id).orElse(null);

        Message message = MessageDto.Request.toEntity(messageDto, sender, receiver, messageRoom);
        messageRepository.save(message);

        MessageDto.Response response = MessageDto.Response.builder()
                .content(messageDto.getContent())
                .sender(sender.getNickname())
                .receiver(messageDto.getReceiver())
                .created_time(message.getCreatedTime())
                .build();

        responseDto.setData(response);
        return responseDto;
    }
}
