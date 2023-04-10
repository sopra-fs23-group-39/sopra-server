package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class GameService {

  private final Logger log = LoggerFactory.getLogger(GameService.class);

  private final GameRepository gameRepository;

  @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }



  public Game createGame(Long hostId, GameMode gameMode, int questionAmount){
      //TODO: might need to be rewritten, this is a first version to check whether game creation works
      Game game = new Game();
      game.setHostId(hostId);
      game.setGameMode(gameMode);
      game = gameRepository.save(game);
      gameRepository.flush();
      return game;
  }
}
