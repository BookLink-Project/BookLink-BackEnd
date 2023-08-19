package BookLink.BookLink.Domain.Chat;

import lombok.*;


public class ChatMessageDto {

//    public enum MessageType {
//        ENTER, TALK
//    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        private Long room_id;

        private String sender; // 보내는 사람 닉네임

        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long room_id;

        private String sender;

        private String message;
    }



}