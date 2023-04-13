package ch.uzh.ifi.hase.soprafs23.questions;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public class QuestionController {

    QuestionService questionService = new QuestionService();

    public Question getMovieQuestion() throws JsonProcessingException {
        String questionText = "What is the title of this movie?";

        List<String> listOfMovieIds = questionService.getAllMovieIds();
        String movieId = questionService.getRandomItem(listOfMovieIds);
        List<String> movieLinks = questionService.getImageLinks(movieId);
        String questionLink = questionService.getRandomItem(movieLinks);

        String correctAnswer = questionService.getMovieTitle(movieId);

        List<String> wrongAnswers = questionService.getSimilarMovies(movieId);

        return new Question(questionText, questionLink, correctAnswer, wrongAnswers);
    }
}