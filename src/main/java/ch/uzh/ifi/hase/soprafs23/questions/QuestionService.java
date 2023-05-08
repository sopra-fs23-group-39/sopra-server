package ch.uzh.ifi.hase.soprafs23.questions;

import ch.uzh.ifi.hase.soprafs23.api.ApiServiceSubstitute;
import ch.uzh.ifi.hase.soprafs23.api.ActorApiService;
import ch.uzh.ifi.hase.soprafs23.api.MovieApiService;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionService {

    private final MovieApiService movieApiService = new MovieApiService();
    private final ActorApiService actorApiService = new ActorApiService();
    private static final String KEY = "k_3zhp2s2n";

//    private List<String> listOfMovieIds;
//
//    {
//        try {
//            listOfMovieIds = movieApiService.getAllMovieIds(KEY);
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
            case POSTER, TRAILER:
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

    public Question getAppropriateQuestion(GameMode gameMode) throws JsonProcessingException {
        Question question;
        int count = 0;
        switch (gameMode) {
            case ACTOR:
                question = getActorQuestion();
                break;
            case MIXED:
                Random random = new Random();
                boolean coinFlip = random.nextBoolean();
                if(coinFlip){
                    question = getActorQuestion();
                } else {
                    question = getMovieQuestion();
                }
                break;
            case POSTER, TRAILER:
                question = getMovieQuestion();
                break;
            default:
                question = getMovieQuestion();
                break;
        }
        return question;
    }

    public Question getMovieQuestion() throws JsonProcessingException {
        String questionText = "What is the title of this movie?";
        String movieId = movieApiService.getRandomItem(listOfMovieIds);
        String questionLink = movieApiService.getImageLink(movieId, KEY);
        String correctAnswer = movieApiService.getItemName(movieId, KEY);
        List<String> wrongAnswers = movieApiService.getSimilarItems(movieId, KEY);
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }

    public Question getActorQuestion() throws JsonProcessingException {
        String questionText = "What is the name of this actor/actress?";

        //List<String> listOfActorIds = actorApiService.getListOfActorIds(KEY, listOfMovieIds);
        //List<List<String>> sortedListOfAllActors = actorApiService.getSortedListOfAllActors(KEY, listOfActorIds);
        //List<String> listOfActors = actorApiService.getListOfActors(sortedListOfAllActors);
        //List<String> listOfActresses = actorApiService.getListOfActresses(sortedListOfAllActors);
        //List<String> listOfValidActorIds = actorApiService.getListOfValidActorIds(listOfActors, listOfActresses);
        //String actorId = actorApiService.getRandomItem(listOfValidActorIds);

        String actorId = actorApiService.getRandomItem(listOfUniqueActorIds);
        String questionLink = actorApiService.getImageLink(actorId, KEY);
        String correctAnswer = actorApiService.getItemName(actorId, KEY);
        List<String> wrongAnswers = actorApiService.getSimilarItems(actorId, KEY, listOfActors, listOfActresses);
        List<String> answers = new ArrayList<>(wrongAnswers);
        answers.add(correctAnswer);
        Collections.shuffle(answers);

        return new Question(questionText, questionLink, correctAnswer, answers.get(0), answers.get(1), answers.get(2),answers.get(3));
    }
}