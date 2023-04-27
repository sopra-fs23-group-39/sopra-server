package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import ch.uzh.ifi.hase.soprafs23.questions.QuestionService;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

        game.getPlayers().add(userService.getUserById(hostId));
        userService.getUserById(hostId).setGame(game);
        game = gameRepository.save(game);
        gameRepository.flush();
        //Host number of games increased
        userService.getUserById(hostId).setNumberGames(userService.getUserById(hostId).getNumberGames()+1);
        userService.getUserById(hostId).setTotalPointsCurrentGame((long)0);
        return game;
    }

    public void findAndJoinGame(Long gameId, Long userId, User user) {
        Game game = gameRepository.findByGameId(gameId);
        String message = String.format("No games with id %d was found", gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        game.getPlayers().add(user);
        user.setGame(game);
        //Player number of games increased
        userService.getUserById(userId).setNumberGames(userService.getUserById(userId).getNumberGames()+1);
        userService.getUserById(userId).setTotalPointsCurrentGame((long)0);
    }

    public List<User> getHostAndPlayers(long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        String message = String.format("No games with id %d was found", gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        //User host = userService.getUserById(game.getHostId());
        List<User> players = game.getPlayers();
        //players.add(0, host);
        return players;
    }
    public void removeAllPlayers(Long gameId){
        for(User player : gameRepository.findByGameId(gameId).getPlayers()){
            player.setGame(null);
            player.setTotalPointsCurrentGame((long) 0);
        }
    }

    public void removePlayer(Long playerId){
        userService.getUserById(playerId).setTotalPointsCurrentGame((long) 0);
        userService.getUserById(playerId).setGame(null);
    }

    public Game getGameSettings(long gameId) {
        String message = "Game with id %d was not found!";
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

    public User getWinner(long gameId) {
        List<User> users = getHostAndPlayers(gameId);

        User winner = null;
        Long maxPoints = Long.MIN_VALUE;

        for (User user : users) {
            Long totalPoints = user.getTotalPointsCurrentGame();
            if (totalPoints > maxPoints) {
                maxPoints = totalPoints;
                winner = user;
            }
        }
        return winner;
    }
    public Game getGameById(Long gameId){
        return gameRepository.findByGameId(gameId);
    }
}