package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.Gender;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceTest {

    QuestionService questionService = new QuestionService();

    @Test
    void testGetAppropriateQuestionShow() throws JsonProcessingException {
        Question question = questionService.getAppropriateQuestion(GameMode.SHOW);
        assertTestPasses(question, "What is the title of this TV series?");
    }

    @Test
    void testGetAppropriateQuestionMovie() throws JsonProcessingException {
        Question question= questionService.getAppropriateQuestion(GameMode.MOVIE);
        assertTestPasses(question, "What is the title of this movie?");
    }

    @Test
    void testGetAppropriateQuestionActor() throws JsonProcessingException {
        Question questionActor = questionService.getAppropriateQuestion(GameMode.ACTOR);
        String correctAnswer = questionActor.getCorrectAnswer();

        assertNotNull(questionActor.getQuestionText());
        List<String> questionTexts = Arrays.asList("What is the name of this actor?", "What is the name of this actress?");

        assertTrue(questionTexts.contains(questionActor.getQuestionText()));
        assertNotNull(questionActor.getQuestionLink());
        assertNotNull(correctAnswer);
        assertNotNull(questionActor.getAnswer1());
        assertNotNull(questionActor.getAnswer2());
        assertNotNull(questionActor.getAnswer3());
        assertNotNull(questionActor.getAnswer4());

        List<String> answers = Arrays.asList(questionActor.getAnswer1(), questionActor.getAnswer2(), questionActor.getAnswer3(), questionActor.getAnswer4());
        Set<String> answersSet = new HashSet<>(answers);
        assertEquals(answers.size(), answersSet.size());
        assertTrue(answers.contains(correctAnswer));
    }

    @Test
    void testGetAppropriateQuestionTrailer() throws JsonProcessingException {
        Question question = questionService.getAppropriateQuestion(GameMode.TRAILER);
        assertTestPasses(question, "What is the title of this movie?");
    }

    @Test
    void testGetAppropriateQuestionMixed() throws JsonProcessingException {
        List<Question> listOfQuestions = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            listOfQuestions.add(questionService.getAppropriateQuestion(GameMode.MIXED));
        }

        int countMovie = 0;
//        int countShow = 0;
//        int countActor = 0;

        for (Question question : listOfQuestions) {
            switch (question.getQuestionText()) {
                case "What is the title of this movie?" -> countMovie++;
//                case "What is the title of this TV series?" -> countShow++;
//                case "What is the name of this actor?", "What is the name of this actress?" -> countActor++;
            }
        }
        assertNotEquals(0, countMovie);
//        assertNotEquals(0, countShow);
//        assertNotEquals(0, countActor);
    }

    @Test
    void getQuestionTest() {
        String questionText = "What is the title of this movie?";
        String questionLink = "https://m.media-amazon.com/images/M/MV5BMjIyNjk1OTgzNV5BMl5BanBnXkFtZTcwOTU0OTk1Mw@@._V1_Ratio1.5000_AL_.jpg";
        String correctAnswer = "Inception";
        List<String> wrongAnswers = Arrays.asList("Interstellar", "Shutter Island", "Django Unchained");

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
        Question question = questionService.getMovieQuestion(0);
        assertTestPasses(question, "What is the title of this movie?");
    }

    @Test
    void testGetMovieQuestionCategory1() throws JsonProcessingException {
        Question question = questionService.getMovieQuestion(1);
        assertTestPasses(question, "What is the title of this TV series?");
    }

    @Test
    void testGetActorQuestionGender0() throws JsonProcessingException {
        Question question = questionService.getActorQuestion(Gender.ACTOR);

        assertTestPasses(question, "What is the name of this actor?");
    }

    @Test
    void testGetActorQuestionGender1() throws JsonProcessingException {
        Question question = questionService.getActorQuestion(Gender.ACTRESS);

        assertTestPasses(question, "What is the name of this actress?");
    }

    @Test
    void testGetTrailerQuestion() throws JsonProcessingException {
        Question question = questionService.getTrailerQuestion();

        assertTestPasses(question, "What is the title of this movie?");
    }

    private void assertTestPasses(Question question, String correctQuestionText) {
        String questionText = question.getQuestionText();
        String correctAnswer = question.getCorrectAnswer();
        String answer1 = question.getAnswer1();
        String answer2 = question.getAnswer2();
        String answer3 = question.getAnswer3();
        String answer4 = question.getAnswer4();

        assertNotNull(questionText);
        assertEquals(correctQuestionText, questionText);
        assertNotNull(question.getQuestionLink());
        assertNotNull(correctAnswer);
        assertNotNull(answer1);
        assertNotNull(answer2);
        assertNotNull(answer3);
        assertNotNull(answer4);

        List<String> answers = Arrays.asList(answer1, answer2, answer3, answer4);
        Set<String> answersSet = new HashSet<>(answers);
        assertEquals(answers.size(), answersSet.size());
        assertTrue(answers.contains(correctAnswer));
    }

}