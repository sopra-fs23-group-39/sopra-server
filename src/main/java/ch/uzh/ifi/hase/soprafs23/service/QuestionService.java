package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.Gender;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

public class QuestionService {
    private final MovieApiService movieApiService = new MovieApiService();
    private final ActorApiService actorApiService = new ActorApiService();
    private static final String KEY = "k_3zhp2s2n";

    private static final String PREFIX_IMDBLIST = "IMDbList";
    private final String moviesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568317885", KEY);
    private final String tvSeriesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568316563", KEY);
    private final String actorsListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568313759", KEY);
    private final String actressesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568313185", KEY);

    List<String> addMovies = Arrays.asList("The Silence of the Lambs", "Schindler's List", "Schindler's List", "American History X");

    Random random = new Random();

    public Question getAppropriateQuestion(GameMode gameMode) throws JsonProcessingException {
        Question question;
        int randomIndex;
        switch (gameMode) {
            case SHOW -> question = getMovieQuestion(1);
            case ACTOR -> {
                randomIndex = random.nextInt(Gender.values().length);
                question = getActorQuestion(Gender.values()[randomIndex]);
            }
            case TRAILER -> question = getTrailerQuestion();
            case MIXED -> {
                int category = random.nextInt(3);
                if (category == 0 || category == 1) {
                    question = getMovieQuestion(category);
                } else {
                    randomIndex = random.nextInt(Gender.values().length);
                    question = getActorQuestion(Gender.values()[randomIndex]);
                }
            }
            default -> question = getMovieQuestion(0);
        }
        return question;
    }

    public Question getTrailerQuestion() throws JsonProcessingException {
        String questionText = "What is the title of this movie?";
        String movieId = movieApiService.getRandomItem(moviesListAsJSONObject);
        String questionLink = movieApiService.getEmbedLink(movieApiService.getJSONObject("YouTubeTrailer", movieId, KEY));
        String movieAsJSONObject = movieApiService.getJSONObject("Title", movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieAsJSONObject);
        List<String> listWithFourAnswers = movieApiService.getSimilarItems(movieAsJSONObject, addMovies);
        List<String> wrongAnswers = removeCorrectAnswerFromWrongAnswers(listWithFourAnswers, correctAnswer);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

    }

    public Question getMovieQuestion(int category) throws JsonProcessingException {
        String questionText = "What is the title of this ";
        String listAsJSONObject;
        List<String> additionalMovies;
        if (category == 0) {
            questionText += "movie?";
            listAsJSONObject = moviesListAsJSONObject;
            additionalMovies = addMovies;
        //Here "else" means category 1 (TV series)
        } else {
            questionText += "TV series?";
            listAsJSONObject = tvSeriesListAsJSONObject;
            additionalMovies = Arrays.asList("True Detective", "Breaking Bad", "Homeland", "The Queen's Gambit");
        }

        String movieId = movieApiService.getRandomItem(listAsJSONObject);
        String questionLink = movieApiService.getImageLink(movieApiService.getJSONObject("Images", movieId, KEY));
        String movieAsJSONObject = movieApiService.getJSONObject("Title", movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieAsJSONObject);
        List<String> listWithFourAnswers = movieApiService.getSimilarItems(movieAsJSONObject, additionalMovies);
        List<String> wrongAnswers = removeCorrectAnswerFromWrongAnswers(listWithFourAnswers, correctAnswer);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

    }

    public Question getActorQuestion(Gender gender) throws JsonProcessingException {
        String questionText = "What is the name of this ";
        String listAsJSONObject;

        if (gender == Gender.ACTOR) {
            questionText += "actor?";
            listAsJSONObject = actorsListAsJSONObject;
        } else {
            questionText += "actress?";
            listAsJSONObject = actressesListAsJSONObject;
        }

        String actorId = actorApiService.getRandomItem(listAsJSONObject);
        String actorAsJSONObject = actorApiService.getJSONObject("Name", actorId, KEY);
        String questionLink = actorApiService.getImageLink(actorAsJSONObject);
        String correctAnswer = actorApiService.getItemName(actorAsJSONObject);
        List<String> listWithFourAnswers = actorApiService.getSimilarItems(actorApiService.getAllIds(listAsJSONObject), KEY);
        List<String> wrongAnswers = removeCorrectAnswerFromWrongAnswers(listWithFourAnswers, correctAnswer);

        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

    }

    public Question getQuestion(String questionText, String questionLink, String correctAnswer, List<String> wrongAnswers) {
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }

    public List<String> removeCorrectAnswerFromWrongAnswers(List<String> listWithFourItems, String correctAnswer) {
        List<String> newList = new ArrayList<>(listWithFourItems);
        boolean duplicateExists = newList.remove(correctAnswer);
        if (!duplicateExists) {
            newList.remove(newList.size() - 1);
        }
        return newList;
    }
}