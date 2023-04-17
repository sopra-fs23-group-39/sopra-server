package ch.uzh.ifi.hase.soprafs23.websocket;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }


    @MessageMapping("/game/{gameId}")
    public void handleGameConnect(@DestinationVariable long gameId) {
        List<User> players = gameService.getHostAndPlayers(gameId);
        List<UserGetDTO> playerDTOs = new ArrayList<>();
        for (User player: players){
            playerDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(player));
        }
        this.messagingTemplate.convertAndSend("/topic/game/" + gameId, playerDTOs);
    }

}
