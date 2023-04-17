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

@Configuration
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

}


