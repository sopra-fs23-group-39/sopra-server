package ch.uzh.ifi.hase.soprafs23.websocket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final UserService userService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameService gameService, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.userService = userService;
    }

    @MessageMapping("/game/{gameId}")
    public void handleGameConnect(@DestinationVariable long gameId, @Payload String message) {
        System.out.println("received message: " + message);
        if (message.equals("idkman")){
            System.out.println("ye me neither");
            List<User> players = gameService.getHostAndPlayers(gameId);
            List<UserGetDTO> playerDTOs = new ArrayList<>();
            for (User player: players){
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
