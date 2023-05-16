package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Answer;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("TestPassword");
        userPostDTO.setUsername("TestUsername");

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        User user = new User();
        user.setPassword("TestPassword");
        user.setUsername("TestUsername");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void testCreateAnswer_fromAnswerPostDTO_toAnswer_success() {
        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setGameId(1L);
        answerPostDTO.setUserId(1L);
        answerPostDTO.setCorrectAnswer("Inception");
        answerPostDTO.setUsersAnswer("A Beautiful Mind");

        LocalDateTime questionDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(30);
        Date questionTime = Date.from(questionDateTime.atZone(ZoneId.systemDefault()).toInstant());
        answerPostDTO.setQuestionTime(questionTime);

        LocalDateTime answerDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(32);
        Date answerTime = Date.from(answerDateTime.atZone(ZoneId.systemDefault()).toInstant());
        answerPostDTO.setTime(answerTime);

        Answer answer = DTOMapper.INSTANCE.convertAnswerPostDTOToEntity(answerPostDTO);

        assertEquals(answerPostDTO.getGameId(), answer.getGameId());
        assertEquals(answerPostDTO.getUserId(), answer.getUserId());
        assertEquals(answerPostDTO.getCorrectAnswer(), answer.getCorrectAnswer());
        assertEquals(answerPostDTO.getUsersAnswer(), answer.getUsersAnswer());
        assertEquals(answerPostDTO.getTime(), answer.getTime());
        assertEquals(answerPostDTO.getQuestionTime(), answer.getQuestionTime());
    }

    @Test
    public void testGetQuestion_fromQuestion_toQuestionGetDTO_success() {
        String questionText = "What is the title of this movie?";
        String questionLink = "https://m.media-amazon.com/images/M/MV5BMjIyNjk1OTgzNV5BMl5BanBnXkFtZTcwOTU0OTk1Mw@@._V1_Ratio1.5000_AL_.jpg";
        String correctAnswer = "Inception";
        String answer1 = "Interstellar";
        String answer2 = "Shutter Island";
        String answer3 = "Django Unchained";
        String answer4 = "Inception";

        Question question = new Question(questionText, questionLink, correctAnswer, answer1, answer2, answer3, answer4);

        QuestionGetDTO questionGetDTO = DTOMapper.INSTANCE.convertEntityToQuestionGetDTO(question);

        assertEquals(question.getQuestionText(), questionGetDTO.getQuestionText());
        assertEquals(question.getQuestionLink(), questionGetDTO.getQuestionLink());
        assertEquals(question.getCorrectAnswer(), questionGetDTO.getCorrectAnswer());
        assertEquals(question.getAnswer1(), questionGetDTO.getAnswer1());
        assertEquals(question.getAnswer2(), questionGetDTO.getAnswer2());
        assertEquals(question.getAnswer3(), questionGetDTO.getAnswer3());
        assertEquals(question.getAnswer4(), questionGetDTO.getAnswer4());
    }

}
