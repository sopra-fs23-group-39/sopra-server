package ch.uzh.ifi.hase.soprafs23.websocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final UserService userService;
    private final Map<Long, ArrayList<WebSocketSession>> hostSessions = new HashMap<>();



    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.userService = userService;
    }

    @MessageMapping("/game/{gameId}")
    public void handleGameConnect(@DestinationVariable long gameId, @Headers Map<String, Object> headers, @Payload String message) {
        System.out.println("received message: " + message);
        if(!hostSessions.containsKey(gameId)){
            System.out.print("putting in gameId coll");
            hostSessions.put(gameId, new ArrayList<>());
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("timer ran out");
                    System.out.println("removing people from game now");
                    hostSessions.remove(gameId);
                    gameService.removeAllPlayers(gameId);
                }
            }, 15000);
        }

        String[] messageParts = message.split(" ");
        if(messageParts.length == 2 && messageParts[0].equals("DISCONNECT") && !messageParts[1].isEmpty()){
            String playerId = messageParts[1];
            System.out.println("Setting " + playerId + "'s game to null...");
            gameService.removePlayer(Long.parseLong(playerId));

        }
        if (message.equals("SUBSCRIBE")){
            List<User> players = gameService.getHostAndPlayers(gameId);
            List<UserGetDTO> playerDTOs = new ArrayList<>();
            for (User player: players){
                player.setTotalPointsCurrentGame((long) 0);
                playerDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(player));
            }
            // topic/game/{gameId} - front-end
            this.messagingTemplate.convertAndSend("/topic/game/" + gameId, playerDTOs);
        }
        if(message.equals("START")){
            String someString = "game started.";
            this.messagingTemplate.convertAndSend("/topic/game/" + gameId, someString);
        }
    }

    @MessageMapping("/game/{gameId}/question")
    @SendTo("/topic/game/{gameId}/question")
    public QuestionGetDTO sendQuestion(@DestinationVariable Long gameId) {
        Question question = gameService.getQuestionToSend(gameId);
        Date creationTime = new Date();
        question.setCreationTime(creationTime);
        return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
    }

    @MessageMapping("/game/{gameId}/answer")
    public void getAnswer(@DestinationVariable Long gameId, @Payload AnswerPostDTO answerPostDTO) {
        userService.Score(answerPostDTO);
    }

}
