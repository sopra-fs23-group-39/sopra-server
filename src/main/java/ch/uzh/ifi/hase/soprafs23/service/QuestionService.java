package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

public class QuestionService {
    private final MovieApiService movieApiService = new MovieApiService();
    private final ActorApiService actorApiService = new ActorApiService();
    private static final String KEY = "k_3zhp2s2n";

    private static final String LIST_NUMBER_ACTORS_TV = "ls568318482";
    private static final String LIST_NUMBER_ACTRESSES_TV = "ls568318873";
    private static final String PREFIX_IMDBLIST = "IMDbList";
    private final String moviesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568317885", KEY);
    private final String tvSeriesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568316563", KEY);
    private final String actorsListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568313759", KEY);
    private final String actressesListAsJSONObject = movieApiService.getJSONObject(PREFIX_IMDBLIST, "ls568313185", KEY);

    List<String> addMovies = Arrays.asList("The Silence of the Lambs", "Schindler's List", "Schindler's List", "American History X");

    Random random = new Random();

    public Question getAppropriateQuestion(GameMode gameMode) throws JsonProcessingException {
        Question question;
        int genderType;
        switch (gameMode) {
            case SHOW -> question = getMovieQuestion(1);
            case ACTOR -> {
                genderType = random.nextInt(2);
                question = getActorQuestion(genderType);
            }
            case TRAILER -> question = getTrailerQuestion();
            case MIXED -> {
                int category = random.nextInt(3);
                if (category == 0 || category == 1) {
                    question = getMovieQuestion(category);
                } else {
                    genderType = random.nextInt(2);
                    question = getActorQuestion(genderType);
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
        System.out.println("Embed id : " + questionLink);
        String movieAsJSONObject = movieApiService.getJSONObject("Title", movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieAsJSONObject);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieAsJSONObject, addMovies);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

//        String questionText = "What is the title of this movie?";
//        String questionLink = "8hP9D6kZseM";
//        String correctAnswer = "Inception";
//        List<String> wrongAnswers = Arrays.asList("Interstellar", "Shutter Island", "Django Unchained");
//        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    public Question getMovieQuestion(int category) throws JsonProcessingException {
        String questionText = "What is the title of this ";
        String listAsJSONObject = null;
        List<String> additionalMovies = new ArrayList<>();
        if (category == 0) {
            questionText += "movie?";
            listAsJSONObject = moviesListAsJSONObject;
            additionalMovies = addMovies;
        } else if (category == 1) {
            questionText += "TV series?";
            listAsJSONObject = tvSeriesListAsJSONObject;
            additionalMovies = Arrays.asList("True Detective", "Breaking Bad", "Homeland", "The Queen's Gambit");
        }

        String movieId = movieApiService.getRandomItem(listAsJSONObject);
        String questionLink = movieApiService.getImageLink(movieApiService.getJSONObject("Images", movieId, KEY));
        String movieAsJSONObject = movieApiService.getJSONObject("Title", movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieAsJSONObject);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieAsJSONObject, additionalMovies);
        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);

//        String questionText = "What is the title of this movie?";
//        String questionLink = "https://m.media-amazon.com/images/M/MV5BMjIyNjk1OTgzNV5BMl5BanBnXkFtZTcwOTU0OTk1Mw@@._V1_Ratio1.5000_AL_.jpg";
//        String correctAnswer = "Inception";
//        List<String> wrongAnswers = Arrays.asList("Interstellar", "Shutter Island", "Django Unchained");
//        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
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

//        String questionText = "What is the name of this actor?";
//        String questionLink = "https://www.teenidols4you.com/blink/Actors/leo/leonardo-dicaprio-1381527890.jpg";
//        String correctAnswer = "Leonardo DiCaprio";
//        List<String> wrongAnswers = Arrays.asList("Brad Pitt", "Johnny Depp", "Matthew McConaughey");
//        return getQuestion(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    public Question getQuestion(String questionText, String questionLink, String correctAnswer, List<String> wrongAnswers) {
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }
}