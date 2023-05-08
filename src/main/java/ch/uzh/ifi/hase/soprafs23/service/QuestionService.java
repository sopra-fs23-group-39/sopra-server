package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionService {
    private final MovieApiService movieApiService = new MovieApiService();
    private final ActorApiService actorApiService = new ActorApiService();
    private static final String KEY = "k_3zhp2s2n";

    private static final String LIST_NUMBER_ACTORS_TV = "ls568318482";

    private static final String LIST_NUMBER_ACTRESSES_TV = "ls568318873";
    private static final String PREFIX_IMDbList = "IMDbList";
    private final String moviesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDbList, "ls568317885", KEY);
    private final String TVSeriesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDbList, "ls568316563", KEY);
    private final String actorsListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDbList, "ls568313759", KEY);
    private final String actressesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDbList, "ls568313185", KEY);

    public Question getAppropriateQuestion(GameMode gameMode) throws JsonProcessingException {
        Question question;
        int genderType;
        switch (gameMode) {
            case SHOW -> question = getMovieQuestion(1);
            case ACTOR -> {
                genderType = (int) Math.floor(Math.random() * 2);
                question = getActorQuestion(genderType);
            }
            case MIXED -> {
                Random random = new Random();
                int category = random.nextInt(3);
                if (category == 0 || category == 1) {
                    question = getMovieQuestion(category);
                } else {
                    genderType = (int) Math.floor(Math.random() * 2);
                    question = getActorQuestion(genderType);
                }
            }
            default -> question = getMovieQuestion(0);
        }
        return question;
    }

    public Question getMovieQuestion(int category) throws JsonProcessingException {
        String questionText = "What is the title of this ";
        String listAsJSONObject = null;

        if (category == 0) {
            questionText += "movie?";
            listAsJSONObject = moviesListAsJSONObject;
        } else if (category == 1) {
            questionText += "TV series?";
            listAsJSONObject = TVSeriesListAsJSONObject;

        }

        String movieId = movieApiService.getRandomItem(listAsJSONObject);
        String questionLink = movieApiService.getImageLink(movieApiService.getJSONObject("Images", movieId, KEY));
        String movieAsJSONObject = movieApiService.getJSONObject("Title", movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieAsJSONObject);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieAsJSONObject);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    public Question getActorQuestion(int gender) throws JsonProcessingException {
        String questionText = "What is the name of this ";
        String listAsJSONObject = null;

        if (gender == 0) {
            questionText += "actor?";
            listAsJSONObject = actorsListAsJSONObject;
        } else if (gender == 1) {
            questionText += "actress?";
            listAsJSONObject = actressesListAsJSONObject;
        }

        String actorId = actorApiService.getRandomItem(listAsJSONObject);
        String actorAsJSONObject = actorApiService.getJSONObject("Name", actorId, KEY);
        String questionLink = actorApiService.getImageLink(actorAsJSONObject);
        String correctAnswer = actorApiService.getItemName(actorAsJSONObject);
        List<String> wrongAnswers = actorApiService.getSimilarItems(actorApiService.getAllIds(listAsJSONObject), KEY);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    private Question getQuestion(String questionText, String questionLink, String correctAnswer, List<String> wrongAnswers) {
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }
}