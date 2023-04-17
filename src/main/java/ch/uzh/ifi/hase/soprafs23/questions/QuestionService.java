package ch.uzh.ifi.hase.soprafs23.questions;

import ch.uzh.ifi.hase.soprafs23.api.ApiServiceSubstitute;
import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public class QuestionService {

    private final MovieApiService movieApiService = new MovieApiService();
    private final ActorApiService actorApiService = new ActorApiService();
    private final static String key = "k_3zhp2s2n";
//    private List<String> listOfMovieIds;
//
//    {
//        try {
//            listOfMovieIds = movieApiService.getAllMovieIds(key);
//        }
//        catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private final ApiServiceSubstitute apiHelper = new ApiServiceSubstitute();
    private final List<String> listOfMovieIds = apiHelper.getListOfModernMovies();
    private final List<String> listOfUniqueActorIds = apiHelper.getListOfUniqueActorIds();
    private final List<String> listOfActors = apiHelper.getListOfMaleActors();
    private final List<String> listOfActresses = apiHelper.getListOfFemaleActresses();


    public Question getMovieQuestion() throws JsonProcessingException {
        String questionText = "What is the title of this movie?";
        String movieId = movieApiService.getRandomItem(listOfMovieIds);
        String questionLink = movieApiService.getImageLink(movieId, key);
        String correctAnswer = movieApiService.getItemName(movieId, key);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieId, key);

        return new Question(questionText, questionLink, correctAnswer, wrongAnswers);
    }

    public Question getActorQuestion() throws JsonProcessingException {
        String questionText = "What is the name of this actor/actress?";

        //List<String> listOfActorIds = actorApiService.getListOfActorIds(key, listOfMovieIds);
        //List<List<String>> sortedListOfAllActors = actorApiService.getSortedListOfAllActors(key, listOfActorIds);
        //List<String> listOfActors = actorApiService.getListOfActors(sortedListOfAllActors);
        //List<String> listOfActresses = actorApiService.getListOfActresses(sortedListOfAllActors);
        //List<String> listOfValidActorIds = actorApiService.getListOfValidActorIds(listOfActors, listOfActresses);

        //String actorId = actorApiService.getRandomItem(listOfValidActorIds);
        String actorId = actorApiService.getRandomItem(listOfUniqueActorIds);
        String questionLink = actorApiService.getImageLink(actorId, key);
        String correctAnswer = actorApiService.getItemName(actorId, key);
        List<String> wrongAnswers = actorApiService.getSimilarItems(actorId, key, listOfActors, listOfActresses);

        return new Question(questionText, questionLink, correctAnswer, wrongAnswers);
    }
}