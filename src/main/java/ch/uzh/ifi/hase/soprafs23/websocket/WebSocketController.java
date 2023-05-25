package ch.uzh.ifi.hase.soprafs23.websocket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final UserService userService;
    private final Map<Long, Question> currentGameQuestions = new HashMap<>();
    private final Multimap<Long, String> multiConnectedClients = ArrayListMultimap.create();
    private final Map<String, Long> reverseConnectedClients = new ConcurrentHashMap<>();


    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.userService = userService;
    }

    @MessageMapping("/game/{gameId}")
    public void handleGameConnect(@DestinationVariable long gameId, @Headers Map<String, Object> headers, @Payload String message) {
        String[] messageParts = message.split(" ");
        if (messageParts.length == 2 && messageParts[0].equals("DISCONNECT") && !messageParts[1].isEmpty()) {
            String playerId = messageParts[1];
            gameService.removePlayer(Long.parseLong(playerId));

        }
        if (message.equals("SUBSCRIBE")){
            List<User> players = gameService.getHostAndPlayers(gameId);
            List<UserGetDTO> playerDTOs = new ArrayList<>();
            //this is hacky, but the way we handle circular references in our database
            //causes the player list to be sorted alphabetically, for whatever reason.
            //this block removes the host from the players list we make here, then adds them back
            //at index 0. This is necessary, because the frontend checks who the host is
            //by looking at the first entry of the player list...
            players.stream()
                    .filter(player -> player.getId() == gameService.getGameById(gameId).getHostId())
                    .findFirst()
                    .ifPresent(player -> {
                        players.remove(player);
                        players.add(0, player);
                    });
            for (User player: players){
                player.setTotalPointsCurrentGame((long) 0);
                playerDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(player));
            }
            this.messagingTemplate.convertAndSend("/topic/game/" + gameId, playerDTOs);
        }
        if(message.equals("START")){
            String someString = "game started.";
            gameService.setGameIsStarted(gameId, true);
            this.messagingTemplate.convertAndSend("/topic/game/" + gameId, someString);
        }
    }

    @MessageMapping("/game/{gameId}/question")
    @SendTo("/topic/game/{gameId}/question")
    public QuestionGetDTO sendQuestion(@DestinationVariable Long gameId, SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();
        synchronized (multiConnectedClients){
            if(multiConnectedClients.get(gameId).size() != gameService.getHostAndPlayers(gameId).size()-1){
                this.messagingTemplate.convertAndSend("/topic/game/" + gameId + "/question", "Waiting for players...");
                multiConnectedClients.put(gameId, sessionId);
                reverseConnectedClients.put(sessionId, gameId);


            } else {
                Question question = gameService.getQuestionToSend(gameId);
                Date creationTime = new Date();
                question.setCreationTime(creationTime);
                currentGameQuestions.put(gameId, question);
                multiConnectedClients.put(gameId, sessionId);
                reverseConnectedClients.put(sessionId, gameId);
                return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(currentGameQuestions.get(gameId));
            }
        }


        //unhandled so far, but works since checks are done in the front end, will think about something for this.
        //this is pretty much unreachable anyway
        return null;
    }
    @EventListener
    public void handleQuestionDisconnect(SessionDisconnectEvent event){
        String sessionId = event.getSessionId();
        Long key = reverseConnectedClients.get(sessionId);
        multiConnectedClients.get(key).remove(sessionId);
        reverseConnectedClients.remove(sessionId);
    }


    @MessageMapping("/game/{gameId}/answer")
    public void getAnswer(@DestinationVariable Long gameId, @Payload AnswerPostDTO answerPostDTO) {
        GameFormat gameFormat = gameService.getGameById(gameId).getGameFormat();
        userService.score(answerPostDTO, gameFormat);
        userService.updateAllGamesScore(answerPostDTO, gameFormat);
        List<User> allUsersInDB = userService.getUsers();
        userService.updateAllUsersRank(allUsersInDB);
        userService.updateAllBlitzRanks(allUsersInDB);
    }

    @MessageMapping("/gamerapid/{gameId}/question")
    @SendTo("/topic/gamerapid/{gameId}/question")
    public QuestionGetDTO sendQuestionRapid(@DestinationVariable Long gameId, @Payload String message) {
        if(message.equals("NEWQUESTION")){
            Question question = gameService.getQuestionToSend(gameId);
            Date creationTime = new Date();
            question.setCreationTime(creationTime);
            return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
        }
        //should be unreachable, if not, it shouldn't return anything anyway
        return null;
    }

    @MessageMapping("/gamerapid/{gameId}/answer")
    public void getRapidAnswer(@DestinationVariable Long gameId, @Payload AnswerPostDTO answerPostDTO) {
        GameFormat gameFormat = gameService.getGameById(gameId).getGameFormat();
        userService.score(answerPostDTO, gameFormat);
        userService.updateAllGamesScore(answerPostDTO, gameFormat);
        List<User> allUsersInDB = userService.getUsers();
        userService.updateAllRapidRanks(allUsersInDB);
    }
}
