package ch.uzh.ifi.hase.soprafs23.questions;

import ch.uzh.ifi.hase.soprafs23.api.ApiServiceSubstitute;
import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Question> getListOfQuestions(GameMode gameMode, int numberOfQuestions) throws JsonProcessingException {
        List<Question> questions = new ArrayList<>();
        Question question;
        int count = 0;
        switch (gameMode) {
            case ACTOR:
                while (count < numberOfQuestions) {
                    question = getActorQuestion();
                    if (!questions.contains(question)) {
                        questions.add(question);
                        count++;
                    }
                }
                break;
            case MIXED:
                int partOfQuestions = numberOfQuestions / 2;
                while (count < partOfQuestions) {
                    question = getActorQuestion();
                    if (!questions.contains(question)) {
                        questions.add(question);
                        count++;
                    }
                }
                while (count < numberOfQuestions) {
                    question = getMovieQuestion();
                    if (!questions.contains(question)) {
                        questions.add(question);
                        count++;
                    }
                }
                break;
            case POSTER:
            case TRAILER:
                while (count < numberOfQuestions) {
                    question = getMovieQuestion();
                    if (!questions.contains(question)) {
                        questions.add(question);
                        count++;
                    }
                }
                break;
        }
        Collections.shuffle(questions);

        return questions;
    }

    public Question getMovieQuestion() throws JsonProcessingException {
        String questionText = "What is the title of this movie?";
        String movieId = movieApiService.getRandomItem(listOfMovieIds);
        String questionLink = movieApiService.getImageLink(movieId, key);
        String correctAnswer = movieApiService.getItemName(movieId, key);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieId, key);
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
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
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }
}