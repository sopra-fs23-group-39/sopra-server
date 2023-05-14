package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceIntegrationTest {

    @Test
    void testGetAppropriateQuestionShow() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question questionShow = questionService.getAppropriateQuestion(GameMode.SHOW);

        assertNotNull(questionShow.getQuestionText());
        assertEquals("What is the title of this TV series?", questionShow.getQuestionText());
        assertNotNull(questionShow.getQuestionLink());
        assertNotNull(questionShow.getCorrectAnswer());
        assertNotNull(questionShow.getAnswer1());
        assertNotNull(questionShow.getAnswer2());
        assertNotNull(questionShow.getAnswer3());
        assertNotNull(questionShow.getAnswer4());

        List<String> answers = Arrays.asList(questionShow.getAnswer1(), questionShow.getAnswer2(), questionShow.getAnswer3(), questionShow.getAnswer4());
        assertTrue(answers.contains(questionShow.getCorrectAnswer()));
    }

    @Test
    void testGetAppropriateQuestionMovie() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question questionMovie = questionService.getAppropriateQuestion(GameMode.MOVIE);

        assertNotNull(questionMovie.getQuestionText());
        assertEquals("What is the title of this movie?", questionMovie.getQuestionText());
        assertNotNull(questionMovie.getQuestionLink());
        assertNotNull(questionMovie.getCorrectAnswer());
        assertNotNull(questionMovie.getAnswer1());
        assertNotNull(questionMovie.getAnswer2());
        assertNotNull(questionMovie.getAnswer3());
        assertNotNull(questionMovie.getAnswer3());

        List<String> answers = Arrays.asList(questionMovie.getAnswer1(), questionMovie.getAnswer2(), questionMovie.getAnswer3(), questionMovie.getAnswer4());
        assertTrue(answers.contains(questionMovie.getCorrectAnswer()));
    }

    @Test
    void testGetAppropriateQuestionActor() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question questionActor = questionService.getAppropriateQuestion(GameMode.ACTOR);

        assertNotNull(questionActor.getQuestionText());
        List<String> questionTexts = Arrays.asList("What is the name of this actor?", "What is the name of this actress?");

        assertTrue(questionTexts.contains(questionActor.getQuestionText()));
        assertNotNull(questionActor.getQuestionLink());
        assertNotNull(questionActor.getCorrectAnswer());
        assertNotNull(questionActor.getAnswer1());
        assertNotNull(questionActor.getAnswer2());
        assertNotNull(questionActor.getAnswer3());
        assertNotNull(questionActor.getAnswer4());

        List<String> answers = Arrays.asList(questionActor.getAnswer1(), questionActor.getAnswer2(), questionActor.getAnswer3(), questionActor.getAnswer4());
        assertTrue(answers.contains(questionActor.getCorrectAnswer()));
    }

    @Test
    void testGetAppropriateQuestionMixed() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        List<Question> listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listOfQuestions.add(questionService.getAppropriateQuestion(GameMode.MIXED));
        }

        int countMovie = 0;
        int countShow = 0;
        int countActor = 0;

        for (Question question : listOfQuestions) {
            switch (question.getQuestionText()) {
                case "What is the title of this movie?" -> countMovie++;
                case "What is the title of this TV series?" -> countShow++;
                case "What is the name of this actor?", "What is the name of this actress?" -> countActor++;
            }
        }
        assertNotEquals(0, countMovie);
        assertNotEquals(0, countShow);
        assertNotEquals(0, countActor);
    }

    @Test
    void getQuestionTest() {
        String questionText = "What is the title of this movie?";
        String questionLink = "https://m.media-amazon.com/images/M/MV5BMjIyNjk1OTgzNV5BMl5BanBnXkFtZTcwOTU0OTk1Mw@@._V1_Ratio1.5000_AL_.jpg";
        String correctAnswer = "Inception";
        List<String> wrongAnswers = Arrays.asList("Interstellar", "Shutter Island", "Django Unchained");

        QuestionService questionService = new QuestionService();

        Question question = questionService.getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

        assertEquals(questionText, question.getQuestionText());
        assertEquals(questionLink, question.getQuestionLink());
        assertEquals(correctAnswer, question.getCorrectAnswer());

        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);

        assertEquals(4, answers.size());
        assertTrue(answers.contains(correctAnswer));
        assertTrue(answers.contains(question.getAnswer1()));
        assertTrue(answers.contains(question.getAnswer2()));
        assertTrue(answers.contains(question.getAnswer3()));
        assertTrue(answers.contains(question.getAnswer4()));

        int count = 0;
        for (String answer : answers) {
            if (answer.equals(correctAnswer)) {
                count++;
            }
        }
        assertEquals(1, count);
    }

    @Test
    void testGetMovieQuestionCategory0() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question question = questionService.getMovieQuestion(0);

        assertNotNull(question.getQuestionText());
        assertEquals("What is the title of this movie?", question.getQuestionText());
        assertNotNull(question.getQuestionLink());
        assertNotNull(question.getCorrectAnswer());
        assertNotNull(question.getAnswer1());
        assertNotNull(question.getAnswer2());
        assertNotNull(question.getAnswer3());
        assertNotNull(question.getAnswer4());

        List<String> answers = Arrays.asList(question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4());
        assertTrue(answers.contains(question.getCorrectAnswer()));
    }

    @Test
    void testGetMovieQuestionCategory1() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question question = questionService.getMovieQuestion(1);

        assertNotNull(question.getQuestionText());
        assertEquals("What is the title of this TV series?", question.getQuestionText());
        assertNotNull(question.getQuestionLink());
        assertNotNull(question.getCorrectAnswer());
        assertNotNull(question.getAnswer1());
        assertNotNull(question.getAnswer2());
        assertNotNull(question.getAnswer3());
        assertNotNull(question.getAnswer4());

        List<String> answers = Arrays.asList(question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4());
        assertTrue(answers.contains(question.getCorrectAnswer()));
    }

    @Test
    void testGetActorQuestionGender0() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question question = questionService.getActorQuestion(0);

        assertNotNull(question.getQuestionText());
        assertEquals("What is the name of this actor?", question.getQuestionText());
        assertNotNull(question.getQuestionLink());
        assertNotNull(question.getCorrectAnswer());
        assertNotNull(question.getAnswer1());
        assertNotNull(question.getAnswer2());
        assertNotNull(question.getAnswer3());
        assertNotNull(question.getAnswer3());

        List<String> answers = Arrays.asList(question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4());
        assertTrue(answers.contains(question.getCorrectAnswer()));
    }

    @Test
    void testGetActorQuestionGender1() throws JsonProcessingException {
        QuestionService questionService = new QuestionService();
        Question question = questionService.getActorQuestion(1);

        assertNotNull(question.getQuestionText());
        assertEquals("What is the name of this actress?", question.getQuestionText());
        assertNotNull(question.getQuestionLink());
        assertNotNull(question.getCorrectAnswer());
        assertNotNull(question.getAnswer1());
        assertNotNull(question.getAnswer2());
        assertNotNull(question.getAnswer3());
        assertNotNull(question.getAnswer4());

        List<String> answers = Arrays.asList(question.getAnswer1(), question.getAnswer2(), question.getAnswer3(), question.getAnswer4());
        assertTrue(answers.contains(question.getCorrectAnswer()));
    }
}