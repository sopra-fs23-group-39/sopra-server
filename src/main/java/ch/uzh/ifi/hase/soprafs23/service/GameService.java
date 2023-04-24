package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Answer;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
/*TODO maybe it makes sense to merge the two services to one "entityService", as I need to couple
   them anyway.. */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserService userService;
    private final QuestionService questionService = new QuestionService();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game createGame(Long hostId, GameMode gameMode, int questionAmount, int timer) {
        //TODO: might need to be rewritten, this is a first version to check whether game creation works
        Game game = new Game();
        game.setHostId(hostId);
        game.setGameMode(gameMode);
        game.setQuestionAmount(questionAmount);
        game.setTimer(timer);
        game.setHost(userService.getUserById(hostId));
      try {
          game.setQuestions(questionService.getListOfQuestions(gameMode, questionAmount));
      }
      catch (JsonProcessingException e) {
          throw new RuntimeException(e);
      }
        userService.getUserById(hostId).setGame(game);
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public void findAndJoinGame(Long gameId, Long userId, User user) {
        Game game = gameRepository.findByGameId(gameId);
        String message = String.format("No games with id %d was found", gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        game.getUserIds().add(userId);
        game.getPlayers().add(user);
        user.setGame(game);
    }

    public List<User> getHostAndPlayers(long gameId) {
        //System.out.println(gameId);
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            System.out.println("game is null");
        }
        User host = userService.getUserById(game.getHostId());
        List<User> players = game.getPlayers();
        //players.add(0, host);
        return players;
    }

    public Game getGameSettings(long gameId) {
        String message = "User with id %d was not found!";
        try {
            return gameRepository.findByGameId(gameId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(message, gameId));
        }
    }

  public Question getQuestionToSend(long gameId) {
      Game gameById = gameRepository.findByGameId(gameId);
      Question question = null;
      int currentRound = gameById.getCurrentRound();
      int numberOfQuestions = gameById.getQuestionAmount();
      if (currentRound < numberOfQuestions) {
          question = gameById.getQuestions().get(currentRound);
          gameById.setCurrentRound(++currentRound);
      }
      return question;
  }

  public void Score(Answer answer){
        long score = ReturnScore(answer);
        Long gameId = answer.getGameId();
        Game gameById = gameRepository.findByGameId(gameId);
        gameById.scoreUser(score);
  }

  public long ReturnScore(Answer answer){
      long score;
      String CorrectAnswer = answer.getCorrectAnswer();
      String UserAnswer = answer.getUsersAnswer();
      Date time = answer.getTime();
      if(UserAnswer.equals(CorrectAnswer)){
          score = 500;
      }
      else{
          score = 0;
      }
      return score;
  }

}
