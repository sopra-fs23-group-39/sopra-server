package ch.uzh.ifi.hase.soprafs23.websocket;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.*;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WaitingRoomHandler implements WebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();

    private final GameService gameService;

    public WaitingRoomHandler(GameService gameService){
        System.out.println("idfk anymre");
        this.gameService= gameService;
    }




    @Override

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connection hello");
        sessions.add(session);
        UriTemplate uriTemplate = new UriTemplate("/game/{gameId}");
        System.out.println("connection hello");
        try {
            System.out.println("trying to establish connection...");
            Map<String, String> pathVariables = uriTemplate.match(session.getUri().getPath());
            String gameIdString = pathVariables.get("gameId");
            long gameId = Long.parseLong(gameIdString);
            List<User> players = gameService.getHostAndPlayers(gameId);
            List<UserGetDTO> playerDTOs = new ArrayList<>();
            for (User player: players){
                playerDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(player));
            }
            broadcastPlayerList(playerDTOs);
            System.out.println("WebSocket connection established with session id: " + session.getId());
        } catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("msg received");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket connection error with session id: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        System.out.println("WebSocket connection closed with session id: " + session.getId() + ", close status: " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void broadcastPlayerList(List<UserGetDTO> players) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String playerListJson = objectMapper.writeValueAsString(players);
        TextMessage message = new TextMessage(playerListJson);
        for (WebSocketSession session : sessions) {
            session.sendMessage(message);
        }

    }
}

