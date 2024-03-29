package BookLink.BookLink.Service.Message;

import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.Member.MemberPrincipal;
import BookLink.BookLink.Domain.Message.Message;
import BookLink.BookLink.Domain.Message.MessageDto;
import BookLink.BookLink.Domain.Message.MessageListDto;
import BookLink.BookLink.Domain.Message.MessageRoom;
import BookLink.BookLink.Domain.Message.MessageRoomDto;
import BookLink.BookLink.Domain.Message.MessageStartDto;
import BookLink.BookLink.Domain.Message.MessageStartDto.Request;
import BookLink.BookLink.Domain.Message.MessageStartDto.Response;
import BookLink.BookLink.Domain.Message.MessagesDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.Message.MessageRepository;
import BookLink.BookLink.Repository.Message.MessageRoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MemberRepository memberRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final BookRentRepository bookRentRepository;

    @Override
    public ResponseDto startMessage(MessageStartDto.Request messageDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        String receiver_nickname = messageDto.getReceiver();

        Member receiver = memberRepository.findByNickname(receiver_nickname).orElse(null);

        MessageRoom messageRoomBySenderAndReceiver = messageRoomRepository.findMessageRoomByMembers(
                loginMember, receiver);

        if (messageRoomBySenderAndReceiver != null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("이미 쪽지방이 존재합니다. 다시 확인해주세요.");
            return responseDto;
        }

        if (receiver == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("받는 사람의 정보가 불명확합니다.");
            return responseDto;
        }

        Long rent_id = messageDto.getRent_id();
        BookRent bookRent = bookRentRepository.findById(rent_id).orElse(null);

        MessageRoom messageRoom = new MessageRoom(loginMember, receiver, bookRent);
        messageRoomRepository.save(messageRoom);

        Message message = Request.toEntity(messageDto, loginMember, receiver, messageRoom);
        messageRepository.save(message);


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
                .sender(loginMember.getNickname())
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
    public ResponseDto sendMessage(MessageDto.Request messageDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        String receiver_nickname = messageDto.getReceiver();

        Member receiver = memberRepository.findByNickname(receiver_nickname).orElse(null);

        MessageRoom messageRoomBySenderAndReceiver = messageRoomRepository.findMessageRoomByMembers(
                loginMember, receiver);

        if (messageRoomBySenderAndReceiver == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하는 쪽지방이 아닙니다.");
            return responseDto;
        }

        if (receiver == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("받는 사람의 정보가 불명확합니다.");
            return responseDto;
        }

        Long message_id = messageDto.getRoom_id();
        MessageRoom messageRoom = messageRoomRepository.findById(message_id).orElse(null);

        Message message = MessageDto.Request.toEntity(messageDto, loginMember, receiver, messageRoom);
        messageRepository.save(message);

        MessageDto.Response response = MessageDto.Response.builder()
                .content(messageDto.getContent())
                .sender(loginMember.getNickname())
                .receiver(messageDto.getReceiver())
                .created_time(message.getCreatedTime())
                .message_id(message.getId())
                .build();

        responseDto.setData(response);
        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto messageRoomList(Member loginMember) {
        ResponseDto responseDto = new ResponseDto();
        List<MessageRoomDto> messageRoomDtoList = new ArrayList<>();
        Map<Long, String> room_list = new HashMap<>();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        List<MessageRoom> messageRooms = messageRoomRepository.findMessageRoomsByMemberNickname(
                loginMember.getNickname());

        if (messageRooms == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("참여한 쪽지방이 없습니다.");
            return responseDto;
        }

        Comparator<MessageRoom> comparedMessageRoom = Comparator.comparing(
                messageRoom -> messageRoom.getMessages().get(messageRoom.getMessages().size() - 1).getCreatedTime()
        );

        messageRooms.sort(comparedMessageRoom);

        for (MessageRoom messageRoom : messageRooms) {

            List<Message> messages = messageRoom.getMessages();
            Message message = messages.get(messages.size() - 1);
            String contents = message.getContents();
            Long id = messageRoom.getId();
            String title = messageRoom.getBookRent().getBook().getTitle();

            MessageRoomDto messageRoomDto = new MessageRoomDto(id, title, contents);
            messageRoomDtoList.add(messageRoomDto);
        }

        responseDto.setData(messageRoomDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto entranceMessageRoom(Long room_id, Member loginMember) {
        ResponseDto responseDto = new ResponseDto();
        MessagesDto messagesDto = new MessagesDto();
        List<MessageDto.Response> responseList = new ArrayList<>();

        String role = messageRoomRepository.findRoleInMessageRoom(loginMember);
        boolean role_boolean = false;

        if (Objects.equals(role, "sender")) {
            role_boolean = true;
        }

        MessageRoom messageRoom = messageRoomRepository.findById(room_id).orElse(null);

        if (messageRoom == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하는 쪽지방이 아닙니다.");
            return responseDto;
        }

        List<Message> messages = messageRoom.getMessages();

        for (Message message : messages) {
            MessageDto.Response response = new MessageDto.Response();
            response.setContent(message.getContents());
            response.setSender(message.getSender().getNickname());
            response.setReceiver(message.getReceiver().getNickname());
            response.setCreated_time(message.getCreatedTime());
            response.setMessage_id(message.getId());

            responseList.add(response);
        }

        messagesDto.setMessages(responseList);
        messagesDto.setApplyInfo(role_boolean);

        responseDto.setData(messagesDto);
        return responseDto;
    }

    @Override
    public ResponseDto deleteMessage(Long message_id, Member loginMember) {
        ResponseDto responseDto = new ResponseDto();

        Message message = messageRepository.findById(message_id).orElse(null);
        if (message == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        Member sender = message.getSender();

        if (!Objects.equals(loginMember.getNickname(), sender.getNickname())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 접근입니다.");
            return responseDto;
        }

        messageRepository.deleteById(message_id);

        return responseDto;
    }
}
