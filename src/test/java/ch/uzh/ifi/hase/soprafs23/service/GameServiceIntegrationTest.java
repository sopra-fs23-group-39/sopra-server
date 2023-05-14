//package ch.uzh.ifi.hase.soprafs23.service;
//
//import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
//import ch.uzh.ifi.hase.soprafs23.entity.Game;
//import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@WebAppConfiguration
//@SpringBootTest
//public class GameServiceIntegrationTest {
//
//    @Qualifier("userRepository")
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private GameService gameService;
//
//
//    @BeforeEach
//    public void setup() {
//        userRepository.deleteAll();
//
//
//    }
//
//    /*@Test
//    public void createGame_Success() throws Exception {
//        User testUser = new User();
//        testUser.setUsername("testUsername");
//        testUser.setPassword("testPassword");
//        testUser.setId(1L);
//
//        User createdUser = userService.createUser(testUser);
//
//        Game testGame = new Game();
//        testGame.setHostId(1L);
//        testGame.setGameMode(GameMode.valueOf("MOVIE"));
//        testGame.setQuestionAmount(10);
//        testGame.setTimer(30);
//
//        Game createdGame = gameService.createGame(testGame.getHostId(), testGame.getGameMode(), testGame.getQuestionAmount(), testGame.getTimer());
//
//        assertEquals(testGame.getHostId(), createdGame.getHostId());
//        assertEquals(testGame.getGameMode(), createdGame.getGameMode());
//        assertEquals(testGame.getQuestionAmount(), createdGame.getQuestionAmount());
//        assertEquals(testGame.getTimer(), createdGame.getTimer());
//    }
//
//    ;*/
//}