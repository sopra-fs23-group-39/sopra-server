//package ch.uzh.ifi.hase.soprafs23.websocket;
//import ch.uzh.ifi.hase.soprafs23.entity.Question;
//import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
//import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
//import ch.uzh.ifi.hase.soprafs23.service.GameService;
//import ch.uzh.ifi.hase.soprafs23.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(WebSocketController.class)
//public class WebSocketControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GameService gameService;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private Map<Long, Question> currentGameQuestions;
//
//    @MockBean
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Test
//    public void testSendQuestionToNonHost() throws Exception {
//        long gameId = 1L;
//        Question question = new Question("text", "link", "corrAns", "ans1", "ans2", "ans3", "ans4");
//        // Set up mock data for currentGameQuestions.get(gameId)
//        when(currentGameQuestions.get(gameId)).thenReturn(question);
//
//        // Perform the WebSocket request
//        ResultActions resultActions = mockMvc.perform(get("/app/game/{gameId}/question/non-host", gameId));
//
//        // Verify the response status
//        resultActions.andExpect(status().isOk());
//
//        QuestionGetDTO questionGetDTO = DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);
//
//        // Verify that the messagingTemplate.convertAndSendToUser() method is called with the correct arguments
//        verify(messagingTemplate).convertAndSendToUser(
//                eq(String.valueOf(gameId)),
//                "/topic/game/{gameId}/question/non-host",
//                questionGetDTO);
//
//        // Additional assertions can be performed based on the desired behavior of the WebSocket endpoint
//    }
//}
