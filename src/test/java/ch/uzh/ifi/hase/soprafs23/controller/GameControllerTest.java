package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @Test
    void gamePOST_createValidGame() throws Exception {
        User host = new User();
        host.setId(1L);
        Game game = new Game();
        game.setGameId(1L);
        game.setHostId(host.getId());
        game.setGameMode(GameMode.MIXED);
        game.setQuestionAmount(5);
        game.setTimer(10);
        game.setGameFormat(GameFormat.CUSTOM);
        game.setHost(host);
        game.setCurrentRound(0);

        given(gameService.createGame(Mockito.anyLong(), Mockito.any(GameMode.class), Mockito.anyInt(),
                Mockito.anyInt(), Mockito.any(GameFormat.class))).willReturn(game);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHostId(1L);
        gamePostDTO.setGameMode(GameMode.MIXED);
        gamePostDTO.setQuestionAmount(5);
        gamePostDTO.setTimer(10);
        gamePostDTO.setGameFormat(GameFormat.CUSTOM);

        MockHttpServletRequestBuilder request = post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hostId", is(1)))
                .andExpect(jsonPath("$.gameMode", is("MIXED")))
                .andExpect(jsonPath("$.questionAmount", is(5)))
                .andExpect(jsonPath("$.timer", is(10)))
                .andExpect(jsonPath("$.gameFormat", is("CUSTOM")));
    }

    @Test
    void gameSettingsGET_getValidGameSettings() throws Exception {
        User host = new User();
        host.setId(1L);
        Game game = new Game();
        game.setGameId(1L);
        game.setHostId(host.getId());
        game.setHost(host);
        game.setGameFormat(GameFormat.CUSTOM);
        game.setGameMode(GameMode.MIXED);
        game.setQuestionAmount(5);
        game.setTimer(10);
        game.getPlayers().add(host);
        game.setCurrentRound(0);

        given(gameService.getGameSettings(1L)).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/game/1/settings");

        // Create the request and verify the response
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostId", is(1)))
                .andExpect(jsonPath("$.gameMode", is("MIXED")))
                .andExpect(jsonPath("$.questionAmount", is(5)))
                .andExpect(jsonPath("$.timer", is(10)))
                .andExpect(jsonPath("$.gameFormat", is("CUSTOM")));
    }

    @Test
    void joinGamePUT_valid() throws Exception {
        Long gameId = 1L;
        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        doNothing().when(gameService).findAndJoinGame(gameId, userId, user);
        given(userService.getUserById(userId)).willReturn(user);

        MockHttpServletRequestBuilder requestBuilder = put("/game/" + gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Long.toString(userId));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).findAndJoinGame(gameId, userId, user);
        verify(userService, times(1)).getUserById(userId);
    }

//    @Test
//    void winnerGET_valid() throws Exception {
//        Long gameId = 1L;
//        User winner = new User();
//        winner.setId(1L);
//        winner.setUsername("Winner");
//        given(gameService.getWinner(gameId)).willReturn(winner);
//
//        mockMvc.perform(get("/game/1/winner"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.username", is("winner")));
//    }

//    @Test
//    void playerListGET_getPlayerList() throws Exception {
//        User host = new User();
//        host.setId(1L);
//        User player1 = new User();
//        player1.setId(2L);
//        User player2 = new User();
//        player2.setId(3L);
//        List<User> players = Arrays.asList(host, player1, player2);
//
//        Game game = new Game();
//        game.setGameId(1L);
//        game.setHost(host);
//        game.setHostId(host.getId());
//        game.setGameFormat(GameFormat.CUSTOM);
//        game.setGameMode(GameMode.MIXED);
//        game.setQuestionAmount(5);
//        game.setTimer(10);
//        game.getPlayers().addAll(players);
//        game.setCurrentRound(0);
//
//        given(gameService.getHostAndPlayers(1L)).willReturn(players);
//
//        UserGetDTO hostDTO = new UserGetDTO();
//        hostDTO.setId(host.getId());
//        UserGetDTO player1DTO = new UserGetDTO();
//        player1DTO.setId(player1.getId());
//        UserGetDTO player2DTO = new UserGetDTO();
//        player2DTO.setId(player2.getId());
//        List<UserGetDTO> playerDTOs = Arrays.asList(hostDTO, player1DTO, player2DTO);
//
//        given(dtoMapper.convertEntityToUserGetDTO(host)).willReturn(hostDTO);
//        given(dtoMapper.convertEntityToUserGetDTO(player1)).willReturn(player1DTO);
//        given(dtoMapper.convertEntityToUserGetDTO(player2)).willReturn(player2DTO);
//
//        mockMvc.perform(get("/game/1"))
//                .andExpect(status().isOk());
////                .andExpect(jsonPath("$[0].id", is(1)))
////                .andExpect(jsonPath("$[1].id", is(2)))
////                .andExpect(jsonPath("$[2].id", is(3)));
//    }


    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}