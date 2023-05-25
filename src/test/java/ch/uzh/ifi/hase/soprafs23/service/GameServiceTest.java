package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Mock
    private UserService userService;

    private Game game;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        game = new Game();
        game.setGameId(1L);
    }

    @Test
    void createGame_validInputs_success() {
        User host = new User();
        long hostId = 1L;
        host.setId(hostId);
        host.setNumberGames(5L);
        host.setTotalPointsCurrentGame(100L);

        GameMode gameMode = GameMode.MOVIE;
        int questionAmount = 20;
        int timer = 10;
        GameFormat gameFormat = GameFormat.CUSTOM;

        Game testGame = new Game();
        testGame.setHostId(hostId);
        testGame.setGameMode(gameMode);
        testGame.setQuestionAmount(questionAmount);
        testGame.setTimer(timer);
        testGame.setHost(host);
        testGame.setGameFormat(gameFormat);
        testGame.setIsStarted(false);
        testGame.getPlayers().add(host);

        when(userService.getUserById(hostId)).thenReturn(host);
        when(gameRepository.save(Mockito.any())).thenReturn(testGame);

        Game createdGame = gameService.createGame(hostId, gameMode, questionAmount, timer, gameFormat);

        verify(gameRepository, times(1)).save(Mockito.any());
        verify(gameRepository, times(1)).flush();

        assertEquals(hostId, createdGame.getHostId());
        assertEquals(gameMode, createdGame.getGameMode());
        assertEquals(questionAmount, createdGame.getQuestionAmount());
        assertEquals(timer, createdGame.getTimer());
        assertEquals(host, createdGame.getHost());
        assertEquals(gameFormat, createdGame.getGameFormat());
        assertFalse(createdGame.getIsStarted());
        assertEquals(new ArrayList<>(List.of(host)), createdGame.getPlayers());
        assertEquals(6L, createdGame.getHost().getNumberGames());
        assertEquals(0L, createdGame.getHost().getTotalPointsCurrentGame());
    }

    @Test
    void createGame_invalidInputs_HostNotFound() {
        long hostId = 1L;
        GameMode gameMode = GameMode.MOVIE;
        int questionAmount = 20;
        int timer = 10;
        GameFormat gameFormat = GameFormat.CUSTOM;

        when(userRepository.findById(hostId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(hostId, gameMode, questionAmount, timer, gameFormat),
                String.format("User with id %d was not found!", hostId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testFindAndJoinGame() {
        User user1 = new User();
        long userId = 1L;
        user1.setId(userId);
        user1.setTotalPointsCurrentGame(15L);
        user1.setNumberGames(5L);

        User user2 = new User();

        List<User> playerList = new ArrayList<>(List.of(user2));

        long gameId = game.getGameId();
        game.setPlayers(playerList);

        Mockito.when(gameRepository.findByGameId(gameId)).thenReturn(game);
        Mockito.when(userService.getUserById(userId)).thenReturn(user1);

        gameService.findAndJoinGame(gameId, userId, user1);

        List<User> newPlayerList = new ArrayList<>(Arrays.asList(user2, user1));

        assertEquals(newPlayerList, game.getPlayers());
        assertEquals(game, user1.getGame());
        assertEquals(6L, user1.getNumberGames());
        assertEquals(0L, user1.getTotalPointsCurrentGame());
    }

    @Test
    void testFindAndJoinGame_GameNotFound() {
        long gameId = game.getGameId();
        long userId = 1L;
        User user = new User();

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> gameService.findAndJoinGame(gameId, userId, user),
                "No games with id " + gameId + " was found!");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No games with id " + gameId + " was found!", exception.getReason());
    }

    @Test
    void testGetHostAndPlayers_GameExists() {
        long gameId = game.getGameId();

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
        long gameId = game.getGameId();

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> gameService.getHostAndPlayers(gameId),
                "No games with id " + gameId + " was found!");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No games with id " + gameId + " was found!", exception.getReason());
    }

    @Test
    void testRemovePlayer() {
        User user = new User();
        long userId = 1L;
        user.setId(userId);

        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        gameService.removePlayer(userId);

        assertEquals(0L, user.getTotalPointsCurrentGame());
        assertNull(user.getGame());
    }

    @Test
    void testGetGameById_GameFound() {
        Long gameId = game.getGameId();

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        assertEquals(game, gameService.getGameById(gameId));
    }

    @Test
    void testGetGameById_GameNotFound() {
        Long gameId = game.getGameId();

        when(gameRepository.findByGameId(gameId)).thenThrow(new RuntimeException());

        assertThrows(ResponseStatusException.class, () -> gameService.getGameById(gameId),  "No games with id " + gameId + " was found");
    }

    @Test
    void testGetQuestionToSend_GameFinished() {
        long gameId = 1L;
        Game testGame = new Game();
        testGame.setGameId(gameId);
        testGame.setCurrentRound(10);
        testGame.setQuestionAmount(5);
        testGame.setGameMode(GameMode.MOVIE);

        when(gameRepository.findByGameId(gameId)).thenReturn(testGame);

        Question receivedQuestion = gameService.getQuestionToSend(gameId);

        verify(gameRepository, times(1)).findByGameId(Mockito.any());

        assertNull(receivedQuestion);

    }

    @Test
    void testSetGameIsStarted() {
        long gameId = game.getGameId();
        boolean isStarted = true;

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        gameService.setGameIsStarted(gameId, isStarted);

        assertEquals(isStarted, game.getIsStarted());
    }

}
