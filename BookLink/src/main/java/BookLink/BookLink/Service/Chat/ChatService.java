package BookLink.BookLink.Service.Chat;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Chat.ChatMessage;
import BookLink.BookLink.Domain.Chat.ChatMessageDto;
import BookLink.BookLink.Domain.Chat.ChatRoom;
import BookLink.BookLink.Domain.Chat.ChatRoomDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.Chat.ChatMessageRepository;
import BookLink.BookLink.Repository.Chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService { // DB 연결 후 변경해야 함

    private BookRepository bookRepository;
    private ChatRoomRepository chatRoomRepository;
    private ChatMessageRepository chatMessageRepository;

    private Map<String, ChatRoomDto> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public ResponseDto findAllRoom() {

        ResponseDto responseDto = new ResponseDto();

        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms) {

            ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                    .room_id(chatRoom.getId())
                    .book_id(chatRoom.getBook().getId())
                    .rent_nickname(chatRoom.getRenter().getNickname())
                    .lend_nickname(chatRoom.getLender().getNickname())
                    .build();

            chatRoomDtoList.add(chatRoomDto);
        }

        responseDto.setData(chatRoomDtoList);
        return responseDto;
    }

    public ResponseDto findChat(Long room_id) {

        ResponseDto responseDto = new ResponseDto();

        ChatRoom chatRoom = chatRoomRepository.findById(room_id).orElse(null);
        if (chatRoom == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 채팅방입니다.");
            return responseDto;
        }

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoom(chatRoom);
        List<ChatMessageDto.Response> chatMessageDtoList = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageDto.Response chatMessageDto = ChatMessageDto.Response.builder()
                    .room_id(room_id)
                    .sender(chatMessage.getSender().getNickname())
                    .message(chatMessage.getMessage())
                    .build();

            chatMessageDtoList.add(chatMessageDto);
        }

        responseDto.setData(chatMessageDtoList);
        return responseDto;

    }

    public ResponseDto createRoom(Long book_id, Member renter) {

        ResponseDto responseDto = new ResponseDto();

        Book book = bookRepository.findById(book_id).orElse(null);

        if (book == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 책입니다.");
            return responseDto;
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .book(book)
                .renter(renter)
                .lender(book.getWriter())
                .build();

        chatRoomRepository.save(chatRoom);

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .room_id(chatRoom.getId())
                .book_id(book_id)
                .rent_nickname(renter.getNickname())
                .lend_nickname(book.getWriter().getNickname())
                .build();

        responseDto.setData(chatRoomDto);
        return responseDto;
    }

    public ResponseDto sendMessage(ChatMessageDto.Request chatMessageDto, Member sender) {

        ResponseDto responseDto = new ResponseDto();

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoom_id()).orElse(null);
        if (chatRoom == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 채팅방입니다.");
            return responseDto;
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(chatMessageDto.getMessage())
                .build();

        chatMessageRepository.save(chatMessage);

        return responseDto;
    }
}