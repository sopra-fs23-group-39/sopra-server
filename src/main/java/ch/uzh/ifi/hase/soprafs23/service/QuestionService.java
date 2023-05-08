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
    private static final String LIST_NUMBER_MOVIES = "ls568317885";
    private static final String LIST_NUMBER_TV = "ls568316563";
    private static final String LIST_NUMBER_ACTORS = "ls568313759";
    private static final String LIST_NUMBER_ACTRESSES = "ls568313185";

    private static final String LIST_NUMBER_ACTORS_TV = "ls568318482";

    private static final String LIST_NUMBER_ACTRESSES_TV = "ls568318873";

    private final List<String> listOfMovieIds;

    {
        try {
            listOfMovieIds = movieApiService.getAllIds(KEY, LIST_NUMBER_MOVIES);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<String> listOfTVSeries;

    {
        try {
            listOfTVSeries = movieApiService.getAllIds(KEY, LIST_NUMBER_TV);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<String> listOfActresses;

    {
        try {
            listOfActresses = actorApiService.getAllIds(KEY, LIST_NUMBER_ACTRESSES);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<String> listOfActors;

    {
        try {
            listOfActors = actorApiService.getAllIds(KEY, LIST_NUMBER_ACTORS);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
        List<String> listToChooseFrom = null;

        if (category == 0) {
            questionText += "movie?";
            listToChooseFrom = listOfMovieIds;
        } else if (category == 1) {
            questionText += "TV series?";
            listToChooseFrom = listOfTVSeries;

        }

        String movieId = movieApiService.getRandomItem(listToChooseFrom);
        String questionLink = movieApiService.getImageLink(movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieId, KEY);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieId, KEY);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    public Question getActorQuestion(int gender) throws JsonProcessingException {
        String questionText = "What is the name of this ";
        List<String> listToChooseFrom = null;

        if (gender == 0) {
            questionText += "actor?";
            listToChooseFrom = listOfActors;
        } else if (gender == 1) {
            questionText += "actress?";
            listToChooseFrom = listOfActresses;
        }

        String actorId = actorApiService.getRandomItem(listToChooseFrom);
        String questionLink = actorApiService.getImageLink(actorId, KEY);
        String correctAnswer = actorApiService.getItemName(actorId, KEY);
        List<String> wrongAnswers = actorApiService.getSimilarItems(KEY, listToChooseFrom);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    private Question getQuestion(String questionText, String questionLink, String correctAnswer, List<String> wrongAnswers) {
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }
}