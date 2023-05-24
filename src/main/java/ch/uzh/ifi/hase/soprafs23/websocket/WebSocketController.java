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
    //private final Map<Long, ArrayList<WebSocketSession>> hostSessions = new HashMap<>();
    private final Map<Long, Question> currentGameQuestions = new HashMap<>();
    //private final Map<Long, String> connectedClients = new ConcurrentHashMap<>();
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
            //this is stupid and hacky, but the way we handle circular references in our database
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

            // topic/game/{gameId} - front-end
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
        System.out.println("New connection from " + sessionId);
        /*synchronized (connectedClients) {
            if(connectedClients.isEmpty()){
                Question question = gameService.getQuestionToSend(gameId);
                Date creationTime = new Date();
                question.setCreationTime(creationTime);
                currentGameQuestions.put(gameId, question);
                this.messagingTemplate.convertAndSend("/topic/game/" + gameId + "/question/non-host", "HOSTREADY");
                connectedClients.put(gameId, sessionId);
                return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
            }else{
                connectedClients.put(gameId, sessionId);
                return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(currentGameQuestions.get(gameId));
            }
        }*/

        //connectedClients.put(gameId, sessionId);

        //System.out.println(connectedClients.size());
        //System.out.println("clients connected: " + connectedClients.get(gameId));
        System.out.println("Current multi conn: " + multiConnectedClients.get(gameId).size());


        synchronized (multiConnectedClients){
            if(multiConnectedClients.get(gameId).size() != gameService.getHostAndPlayers(gameId).size()-1){
                System.out.println("currently multi size: " + multiConnectedClients.get(gameId).size());
                System.out.println("game size: " + gameService.getHostAndPlayers(gameId).size());
            /*Question question = gameService.getQuestionToSend(gameId);
            Date creationTime = new Date();
            question.setCreationTime(creationTime);
            currentGameQuestions.put(gameId, question);*/
                //System.out.println("connected client size: " + connectedClients.size());
                this.messagingTemplate.convertAndSend("/topic/game/" + gameId + "/question", "Waiting for players...");
                multiConnectedClients.put(gameId, sessionId);
                reverseConnectedClients.put(sessionId, gameId);

                //return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
            } else {
                System.out.println("are we in here?");
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
        return null;
    }
    @EventListener
    public void handleQuestionDisconnect(SessionDisconnectEvent event){
        String sessionId = event.getSessionId();
        Long key = reverseConnectedClients.get(sessionId);
        System.out.println("key: " + key + " Session: " + reverseConnectedClients.get(sessionId) );
        //connectedClients.values().removeIf(id -> id.equals(sessionId));
        multiConnectedClients.get(key).remove(sessionId);
        reverseConnectedClients.remove(sessionId);
    }

    @MessageMapping("/game/{gameId}/question/non-host")
    @SendTo("/topic/game/{gameId}/question/non-host")
    public QuestionGetDTO sendQuestionToNonHost(@DestinationVariable Long gameId) {
        System.out.println("connect?");
        Question question = currentGameQuestions.get(gameId);
        /*Date creationTime = new Date();
        question.setCreationTime(creationTime);*/
        return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
    }

    @MessageMapping("/game/{gameId}/answer")
    public void getAnswer(@DestinationVariable Long gameId, @Payload AnswerPostDTO answerPostDTO) {
        GameFormat gameFormat = gameService.getGameById(gameId).getGameFormat();
        userService.score(answerPostDTO, gameFormat);
        userService.updateAllGamesScore(answerPostDTO, gameFormat);
        List<User> allUsersInDB = userService.getUsers();
        //Looks like we do unnecessary job updating 2 rankings, although we get message only about one type of game
        // Merge with Rapid? (delete that endpoint?)
        userService.updateAllUsersRank(allUsersInDB);
        userService.updateAllBlitzRanks(allUsersInDB);
    }

    @MessageMapping("/gamerapid/{gameId}/question")
    @SendTo("/topic/gamerapid/{gameId}/question")
    public QuestionGetDTO sendQuestionRapid(@DestinationVariable Long gameId, SimpMessageHeaderAccessor headerAccessor, @Payload String message) {

        String sessionId = headerAccessor.getSessionId();
        if(message.equals("NEWQUESTION")){
            Question question = gameService.getQuestionToSend(gameId);
            Date creationTime = new Date();
            question.setCreationTime(creationTime);
            return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
        }
        //unhandled so far, but works since checks are done in the front end, will think about something for this.
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
