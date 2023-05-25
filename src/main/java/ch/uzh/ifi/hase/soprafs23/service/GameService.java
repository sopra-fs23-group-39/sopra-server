package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to
 * the game
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final UserService userService;
    private final QuestionService questionService = new QuestionService();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game createGame(Long hostId, GameMode gameMode, int questionAmount, int timer, GameFormat gameFormat) {
        User host = userService.getUserById(hostId);
        if (host == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with id %d was not found!", hostId));
        }

        Game game = new Game();
        game.setHostId(hostId);
        game.setGameMode(gameMode);
        game.setQuestionAmount(questionAmount);
        game.setTimer(timer);
        game.setHost(host);
        game.setGameFormat(gameFormat);
        game.setIsStarted(false);

        game.getPlayers().add(host);
        host.setGame(game);
        game = gameRepository.save(game);
        gameRepository.flush();
        host.setNumberGames(host.getNumberGames() + 1);
        host.setTotalPointsCurrentGame((long) 0);
        return game;
    }

    public void findAndJoinGame(Long gameId, Long userId, User user) {
        Game game = gameRepository.findByGameId(gameId);
        String message = String.format("No games with id %d was found!", gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        game.getPlayers().add(user);
        gameRepository.save(game);
        gameRepository.flush();
        user.setGame(game);
        //Player number of games increased
        userService.getUserById(userId).setNumberGames(userService.getUserById(userId).getNumberGames() + 1);
        userService.getUserById(userId).setTotalPointsCurrentGame((long)0);
    }

    public List<User> getHostAndPlayers(long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        String message = String.format("No games with id %d was found!", gameId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return game.getPlayers();
    }

    public void removePlayer(Long playerId){
        userService.getUserById(playerId).setTotalPointsCurrentGame((long) 0);
        if(userService.getUserById(playerId).getGame()!=null){
            userService.getUserById(playerId).setGame(null);
        }
    }

    public Game getGameById(Long gameId) {
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
            try {
                question = questionService.getAppropriateQuestion(gameById.getGameMode());
                gameById.setCurrentRound(++currentRound);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
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

    public void setGameIsStarted(long gameId, boolean isStarted) {
        gameRepository.findByGameId(gameId).setIsStarted(isStarted);
    }
}