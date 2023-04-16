package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameCreationDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class GameController {

  private final GameService gameService;
  private final UserService userService;

  GameController(GameService gameService, UserService userService) {
    this.gameService = gameService;
    this.userService = userService;
  }

    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameCreationDTO createGame(@RequestBody GameCreationDTO gameCreationDTO){
        Game game = gameService.createGame(gameCreationDTO.getHostId(), gameCreationDTO.getGameMode(), gameCreationDTO.getQuestionAmount());
        return DTOMapper.INSTANCE.convertEntityToGameCreationDTO(game);
    }
    @PutMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinGame(@PathVariable long gameId, @RequestBody long userId){
      gameService.findAndJoinGame(gameId, userId, userService.getUserById(userId));
    }

    @GetMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> playerList (@PathVariable long gameId){
        List<User> players= gameService.getHostAndPlayers(gameId);
        List<UserGetDTO> playerDTOs = new ArrayList<>();
        for (User player: players){
            playerDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(player));
        }
        return playerDTOs;
    }
}
