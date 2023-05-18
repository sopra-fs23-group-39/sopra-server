package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //Tested, only CREATED, other status is not tested
    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GamePostDTO createGame(@RequestBody GamePostDTO gamePostDTO){
        Game game = gameService.createGame(gamePostDTO.getHostId(), gamePostDTO.getGameMode(), gamePostDTO.getQuestionAmount(), gamePostDTO.getTimer(), gamePostDTO.getGameFormat());
        return DTOMapper.INSTANCE.convertEntityToGamePostDTO(game);
    }

    // Tested, NO_CONTENT + NOT_FOUND
    @PutMapping("/game/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinGame(@PathVariable long gameId, @RequestBody long userId){
        gameService.findAndJoinGame(gameId, userId, userService.getUserById(userId));
    }

    //Tested NOT_FOUND. OK gives an error
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

    //Tested NO_CONTENT + NOT_FOUND
    @PutMapping("/game/resetIfBackOnMain")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> resetUserPointsAndGame(@RequestBody long playerId) {
        gameService.removePlayer(playerId);
        return ResponseEntity.noContent().build();
    }

    // Tested, OK + NOT_FOUND
    @GetMapping("/game/{gameId}/settings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GamePostDTO retrieveGameSettings (@PathVariable long gameId){
        Game findByGameId = gameService.getGameById(gameId);
        return DTOMapper.INSTANCE.convertEntityToGamePostDTO(findByGameId);
    }

    //Tested OK + NOT_FOUND
    @GetMapping("/game/{lobbyId}/currentRanking")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getByLobbyId(@PathVariable Long lobbyId) {
        List<User> users = userService.retrieveCurrentRanking(lobbyId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    //Tested NOT_FOUND, OK gives an error
    @GetMapping("/game/{lobbyId}/totalRanking")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getRankingByLobbyId(@PathVariable Long lobbyId) {
        List<User> users = userService.retrieveTotalRanking(lobbyId);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    //Tested OK + NOT_FOUND
    @GetMapping("/game/{lobbyId}/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO winner(@PathVariable long lobbyId){
        User user = gameService.getWinner(lobbyId);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

}