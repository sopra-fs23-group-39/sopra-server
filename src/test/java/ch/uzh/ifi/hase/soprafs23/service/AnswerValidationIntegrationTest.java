package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AnswerValidationIntegrationTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private UserService userService = new UserService(userRepository, gameRepository);
    private final AnswerPostDTO answerPostDTO = new AnswerPostDTO();
    private String correctAnswer;
    private String wrongAnswer;

    @BeforeEach
    void answerSetup() {
        answerPostDTO.setUserId(1L);
        answerPostDTO.setGameId(1L);
        answerPostDTO.setCorrectAnswer("Inception");

        LocalDateTime questionDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(30);
        Date questionTime = Date.from(questionDateTime.atZone(ZoneId.systemDefault()).toInstant());
        answerPostDTO.setQuestionTime(questionTime);

        LocalDateTime answerDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(32);
        Date answerTime = Date.from(answerDateTime.atZone(ZoneId.systemDefault()).toInstant());
        answerPostDTO.setTime(answerTime);

        correctAnswer = "Inception";
        wrongAnswer = "Shutter Island";
    }

    @Test
    void correctScoreCalculation_wrongAnswer() {

        answerPostDTO.setUsersAnswer(wrongAnswer);
        assertEquals(0, userService.returnScore(answerPostDTO, GameFormat.CUSTOM));
    }

    @Test
    void correctScoreCalculation_rightAnswer() {
        answerPostDTO.setUsersAnswer(correctAnswer);
        assertEquals(209, userService.returnScore(answerPostDTO, GameFormat.CUSTOM));
    }

    @Test
    void correctScoreCalculationRapid_wrongAnswer() {
        answerPostDTO.setUsersAnswer(wrongAnswer);
        assertEquals(-30, userService.returnScore(answerPostDTO, GameFormat.RAPID));
    }

    @Test
    void correctScoreCalculationRapid_rightAnswer() {
        answerPostDTO.setUsersAnswer(correctAnswer);
        assertEquals(209, userService.returnScore(answerPostDTO, GameFormat.RAPID));
    }

    @Test
    void correctScoreCalculationBlitz_wrongAnswer() {
        answerPostDTO.setUsersAnswer(wrongAnswer);
        assertEquals(0, userService.returnScore(answerPostDTO, GameFormat.BLITZ));
    }

    @Test
    void correctScoreCalculationBlitz_rightAnswer() {
        answerPostDTO.setUsersAnswer(correctAnswer);
        assertEquals(300, userService.returnScore(answerPostDTO, GameFormat.BLITZ));
    }
}
