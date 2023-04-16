package ch.uzh.ifi.hase.soprafs23.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;
@Component
public class WaitingRoomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        UriTemplate uriTemplate = new UriTemplate("/game/{gameId}");
        Map<String, String> pathVariables = uriTemplate.match(request.getURI().getPath());
        String gameIdString = pathVariables.get("gameId");
        long gameId = Long.parseLong(gameIdString);
        // Add the game id to the WebSocket session attributes so it can be used by the WebSocketHandler
        attributes.put("gameId", gameId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }
}