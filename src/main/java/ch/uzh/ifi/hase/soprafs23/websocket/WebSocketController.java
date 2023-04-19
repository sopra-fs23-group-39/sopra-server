package ch.uzh.ifi.hase.soprafs23.websocket;


import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Answer;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final QuestionService questionService = new QuestionService();

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
        // topic/game/{gameId} - front-end
        this.messagingTemplate.convertAndSend("/topic/game/" + gameId, playerDTOs);
    }

    @MessageMapping("/game/{gameId}/question")
    @SendTo("/topic/game/{gameId}/question")
    public QuestionGetDTO sendQuestion(@DestinationVariable Long gameId) throws JsonProcessingException {
        Question question = questionService.getMovieQuestion();
        return DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
    }

    @MessageMapping("/game/{gameId}/answer")
    public Answer getAnswer(@DestinationVariable Long gameId, @Payload AnswerPostDTO answerPostDTO) throws JsonProcessingException {
        return DTOMapper.INSTANCE.convertAnswerPostDTOToEntity(answerPostDTO);
    }

}
