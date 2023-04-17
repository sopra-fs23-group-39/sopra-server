package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Controller
@EnableWebSocket
public class GameSessionController {

    QuestionService qs = new QuestionService();
    private ObjectMapper objectMapper = new ObjectMapper();
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ResponseEntity<String> sendMessage(String message) throws JsonProcessingException {

        Question question = qs.getMovieQuestion();
        System.out.println("Received message: " + message);

        // convert the Question object to a JSON string
        String json = objectMapper.writeValueAsString(question);

        // return the JSON string in a ResponseEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        messagingTemplate.convertAndSend("/topic/messages", question);

        return new ResponseEntity<>(json, headers, HttpStatus.OK);

    }
}
