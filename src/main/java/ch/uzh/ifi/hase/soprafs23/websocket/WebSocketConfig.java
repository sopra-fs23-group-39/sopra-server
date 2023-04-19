package ch.uzh.ifi.hase.soprafs23.websocket;
import ch.uzh.ifi.hase.soprafs23.controller.GameController;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.sockjs.transport.handler.SockJsWebSocketHandler;
import org.springframework.web.socket.sockjs.transport.handler.WebSocketTransportHandler;

/*@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final GameService gameService;
    private final WaitingRoomHandshakeInterceptor waitingRoomHandshakeInterceptor;
    @Autowired
    public WebSocketConfig(GameService gameService, WaitingRoomHandshakeInterceptor waitingRoomHandshakeInterceptor){
        this.gameService = gameService;
        this.waitingRoomHandshakeInterceptor = waitingRoomHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("trying to register");
        registry.addHandler(waitingRoomHandler(), "/game/{gameId}").setAllowedOrigins("*").addInterceptors(waitingRoomHandshakeInterceptor);
    }

    @Bean
    public WebSocketHandler waitingRoomHandler() {
        System.out.println("are we making new?");
        return new WaitingRoomHandler(gameService);
    }

}*/
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
        // end point in the back end (app/game/{gameId}
        registry.addEndpoint("/game/{gameId}").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/game/{gameId}/question").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/game/{gameId}/answer").setAllowedOriginPatterns("*").withSockJS();
    }
}

