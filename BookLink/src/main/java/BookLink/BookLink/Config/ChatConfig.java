package BookLink.BookLink.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") // websocket 연결할 때 사용할 API 경로
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // roomDetail.html var sock = new SockJS("/ws/chat"); 에서 새로운 핸드쉐이크 커넥션 생성할 때 사용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/queue", "/topic"); // 메시지 받을 때의 경로
        // prefix 에 붙으면 해당 채팅방을 구독한 클라이언트에게 메시지 전달
        // queue 1 대 1, topic 1 대 다

        registry.setApplicationDestinationPrefixes("/app"); // 메시지 보낼 때의 경로
    }
}