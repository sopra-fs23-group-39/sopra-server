package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;
    private UserService userService;

    private Game testGame;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        //given
        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setHostId(1L);
        testGame.setGameMode(GameMode.POSTER);
        testGame.setQuestionAmount(5);
        testGame.setTimer(20);





        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

    }

    @Test
    public void createGame_validGame_sucess(){

        Game createdGame = gameService.createGame(testGame.getHostId(),testGame.getGameMode(),testGame.getQuestionAmount(),testGame.getTimer());
        Mockito.verify(gameRepository,Mockito.times(1)).save(Mockito.any());

        assertEquals(testGame.getHostId(),createdGame.getHostId());
        assertEquals(testGame.getGameMode(),createdGame.getGameMode());
        assertEquals(testGame.getQuestionAmount(),createdGame.getQuestionAmount());
        assertEquals(testGame.getTimer(),createdGame.getTimer());
    }
}
