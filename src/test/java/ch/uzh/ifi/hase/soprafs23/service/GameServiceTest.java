package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private UserService userService = new UserService(userRepository, gameRepository);

    private Game game;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        game = new Game();
        game.setGameId(1L);

        // when -> any object is being saved in the userRepository -> return the dummy
        // game
        when(gameRepository.save(Mockito.any())).thenReturn(game);
    }

//    @Test
//    void createGame_validInputs_success() {
//        User host = new User();
//        Long hostId = 1L;
//        GameMode gameMode = GameMode.MOVIE;
//        int questionAmount = 20;
//        int timer = 10;
//        GameFormat gameFormat = GameFormat.CUSTOM;
//        Game createdGame = gameService.createGame(hostId, gameMode, questionAmount, timer, gameFormat);
////
////        when(userService.getUserById(hostId)).thenReturn(host);
//        // then
//        verify(gameRepository, times(1)).save(Mockito.any());
//
//        assertEquals(hostId, createdGame.getGameId());
//        assertEquals(gameMode, createdGame.getGameMode());
//        assertEquals(questionAmount, createdGame.getQuestionAmount());
//        assertEquals(timer, createdGame.getTimer());
//        assertEquals(gameFormat, createdGame.getGameFormat());
//        assertEquals(false, createdGame.getIsStarted());
////        assertEquals(host, createdGame.getHost());
//    }

    @Test
    void testGetHostAndPlayers_GameExists() {
        long gameId = 1L;

        Game game = new Game();
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        game.setPlayers(users);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        List<User> result = gameService.getHostAndPlayers(gameId);
        assertEquals(users, result);
    }

    @Test
    void testGetHostAndPlayers_GameNotFound() {
        long gameId = 1L;

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> gameService.getHostAndPlayers(gameId),
                "No games with id " + gameId + " was found");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No games with id " + gameId + " was found", exception.getReason());
    }

}
