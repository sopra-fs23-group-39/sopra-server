package ch.uzh.ifi.hase.soprafs23.websocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // topic - front-end address starts with it
        config.enableSimpleBroker("/topic");
        // app - back-end address starts with it
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // end point in the back starts with app
        registry.addEndpoint("/game/{gameId}").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/game/{gameId}/question").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/game/{gameId}/answer").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration){
        registration.setMessageSizeLimit(1024*1024);
    }

}

