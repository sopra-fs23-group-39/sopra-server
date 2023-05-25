package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private DTOMapper dtoMapper;

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

//    @Test
//    void gamePOST_createGame_invalidHostId() throws Exception {
//        GamePostDTO gamePostDTO = new GamePostDTO();
//
//        when((gameService).createGame(Mockito.anyLong(), Mockito.any(GameMode.class), Mockito.anyInt(),
//                Mockito.anyInt(), Mockito.any(GameFormat.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        MockHttpServletRequestBuilder postRequest = post("/game")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(gamePostDTO));
//
//        mockMvc.perform(postRequest)
//                .andExpect(status().isNotFound());
//    }

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

        given(gameService.getGameById(1L)).willReturn(game);

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
    void gameSettingsGET_getInvalidGameSettings() throws Exception {
        // HttpStatus 404 Not Found
        when(gameService.getGameById(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/game/1/settings")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

//    @Test
//    void playerList_returnsListOfPlayerDTOs() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Game game = new Game();
//        game.setGameId(1L);
//        Long gameId = 1L;
//
//        User host = new User();
//        host.setId(1L);
//        host.setUsername("host");
//        User player1 = new User();
//        player1.setId(2L);
//        player1.setUsername("player1");
//        User player2 = new User();
//        player2.setId(3L);
//        player2.setUsername("player2");
//        List<User> players = Arrays.asList(host, player1, player2);
//
//        given(gameService.getHostAndPlayers(gameId)).willReturn(players);
//
//        UserGetDTO hostGetDTO = new UserGetDTO();
//        hostGetDTO.setId(1L);
//        hostGetDTO.setUsername("host");
//        UserGetDTO player1GetDTO = new UserGetDTO();
//        player1GetDTO.setId(2L);
//        player1GetDTO.setUsername("player1");
//        UserGetDTO player2GetDTO = new UserGetDTO();
//        player2GetDTO.setId(3L);
//        player2GetDTO.setUsername("player2");
//
//        given(dtoMapper.convertEntityToUserGetDTO(host)).willReturn(hostGetDTO);
//        given(dtoMapper.convertEntityToUserGetDTO(player1)).willReturn(player1GetDTO);
//        given(dtoMapper.convertEntityToUserGetDTO(player2)).willReturn(player2GetDTO);
//
//        MvcResult result = mockMvc.perform(get(String.format("/game/%d", gameId)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        List<UserGetDTO> returnedUsers = objectMapper.readValue(response, new TypeReference<List<UserGetDTO>>() {});
//
//        assertThat(returnedUsers).hasSize(3);
////        assertThat(returnedUsers.get(0).getId()).isEqualTo(host.getId());
////        assertThat(returnedUsers.get(0).getUsername()).isEqualTo(host.getUsername());
////        assertThat(returnedUsers.get(1).getId()).isEqualTo(player1.getId());
////        assertThat(returnedUsers.get(1).getUsername()).isEqualTo(player1.getUsername());
////        assertThat(returnedUsers.get(2).getId()).isEqualTo(player2.getId());
////        assertThat(returnedUsers.get(2).getUsername()).isEqualTo(player2.getUsername());
//    }

    @Test
    void playerList_returnsListOfPlayerDTOs_NotFound() throws Exception {
        when(gameService.getHostAndPlayers(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/game/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
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

    @Test
    void joinGamePUT_invalid() throws Exception {
        // HttpStatus 404 Not Found
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).findAndJoinGame(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/game/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

//    @Test
//    void joinGamePUT_invalid_Rapid() throws Exception {
//        // HttpStatus 401 Unauthorized
//        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(gameService).findAndJoinGame(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
//
//        MockHttpServletRequestBuilder putRequest = put("/game/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(1));
//
//        mockMvc.perform(putRequest)
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    void resetUserPointsAndGame_removesPlayerAndReturnsNoContent() throws Exception {
        Long userId = 1L;

        doNothing().when(gameService).removePlayer(userId);

        MockHttpServletRequestBuilder requestBuilder = put("/game/resetIfBackOnMain")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).removePlayer(userId);
    }

    @Test
    void resetUserPointsAndGame_removesPlayerAndReturnsNotFound() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).removePlayer(Mockito.anyLong());

        MockHttpServletRequestBuilder putRequest = put("/game/resetIfBackOnMain")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrentRanking_returnsCorrectUserList() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Long gameId = 1L;
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setCurrentPoints(20L);
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setCurrentPoints(15L);

        List<User> userList = Arrays.asList(user1, user2);
        given(userService.retrieveCurrentRanking(gameId)).willReturn(userList);

        UserGetDTO userGetDTO1 = new UserGetDTO();
        userGetDTO1.setId(user1.getId());
        userGetDTO1.setUsername(user1.getUsername());
        userGetDTO1.setCurrentPoints(user1.getCurrentPoints());
        UserGetDTO userGetDTO2 = new UserGetDTO();
        userGetDTO2.setId(user2.getId());
        userGetDTO2.setUsername(user2.getUsername());
        userGetDTO2.setCurrentPoints(user2.getCurrentPoints());
        given(dtoMapper.convertEntityToUserGetDTO(user1)).willReturn(userGetDTO1);
        given(dtoMapper.convertEntityToUserGetDTO(user2)).willReturn(userGetDTO2);

        MvcResult result = mockMvc.perform(get(String.format("/game/%d/currentRanking", gameId)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        List<UserGetDTO> returnedUsers = objectMapper.readValue(response, new TypeReference<>() {});

        assertThat(returnedUsers).hasSize(2);
        assertThat(returnedUsers.get(0).getId()).isEqualTo(user1.getId());
        assertThat(returnedUsers.get(0).getUsername()).isEqualTo(user1.getUsername());
        assertThat(returnedUsers.get(0).getCurrentPoints()).isEqualTo(user1.getCurrentPoints());
        assertThat(returnedUsers.get(1).getId()).isEqualTo(user2.getId());
        assertThat(returnedUsers.get(1).getUsername()).isEqualTo(user2.getUsername());
        assertThat(returnedUsers.get(1).getCurrentPoints()).isEqualTo(user2.getCurrentPoints());
    }

    @Test
    void getCurrentRanking_returnsNotFound() throws Exception {
        when(userService.retrieveCurrentRanking(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/game/1/currentRanking")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

//    @Test
//    void getTotalRankingCurrentGame_returnsCorrectUserList() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Long gameId = 1L;
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//        user1.setTotalPointsCurrentGame(20L);
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setUsername("user2");
//        user2.setTotalPointsCurrentGame(15L);
//
//        List<User> userList = new ArrayList<>(Arrays.asList(user1, user2));
//        given(userService.retrieveTotalRanking(gameId)).willReturn(userList);
//
//        UserGetDTO userGetDTO1 = new UserGetDTO();
//        userGetDTO1.setId(user1.getId());
//        userGetDTO1.setUsername(user1.getUsername());
//        userGetDTO1.setTotalPointsCurrentGame(user1.getTotalPointsCurrentGame());
//        UserGetDTO userGetDTO2 = new UserGetDTO();
//        userGetDTO2.setId(user2.getId());
//        userGetDTO2.setUsername(user2.getUsername());
//        userGetDTO2.setTotalPointsCurrentGame(user2.getTotalPointsCurrentGame());
//        given(dtoMapper.convertEntityToUserGetDTO(user1)).willReturn(userGetDTO1);
//        given(dtoMapper.convertEntityToUserGetDTO(user2)).willReturn(userGetDTO2);
//
//        MvcResult result = mockMvc.perform(get(String.format("/game/%d/totalRanking", gameId)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        List<UserGetDTO> returnedUsers = objectMapper.readValue(response, new TypeReference<>() {});
//
//        assertThat(returnedUsers).hasSize(2);
//        assertThat(returnedUsers.get(0).getId()).isEqualTo(user1.getId());
//        assertThat(returnedUsers.get(0).getUsername()).isEqualTo(user1.getUsername());
//        assertThat(returnedUsers.get(0).getTotalPointsCurrentGame()).isEqualTo(user1.getTotalPointsCurrentGame());
//        assertThat(returnedUsers.get(1).getId()).isEqualTo(user2.getId());
//        assertThat(returnedUsers.get(1).getUsername()).isEqualTo(user2.getUsername());
//        assertThat(returnedUsers.get(1).getTotalPointsCurrentGame()).isEqualTo(user2.getTotalPointsCurrentGame());
//    }

//    @Test
//    void getTotalRankingCurrentGame_returnsCorrectUserList() throws Exception {
//        Long gameId = 1L;
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//        user1.setTotalPointsCurrentGame(20L);
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setUsername("user2");
//        user2.setTotalPointsCurrentGame(15L);
//
//        List<User> userList = new ArrayList<>(Arrays.asList(user1, user2));
//        when(userService.retrieveTotalRanking(gameId)).thenReturn(userList);
//
//        MockHttpServletRequestBuilder getRequest = get("/game/1/totalRanking")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(getRequest)
//                .andExpect(status().isOk());
//    }

    @Test
    void getTotalRanking_returnsNotFound() throws Exception {
        when(userService.retrieveTotalRanking(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/game/1/totalRanking")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void testWinner() {
        Long gameId = 1L;
        User winner = new User();
        winner.setId(1L);
        winner.setUsername("Winner");

        given(gameService.getWinner(gameId)).willReturn(winner);

        UserGetDTO winnerGetDTO = new UserGetDTO();
        winnerGetDTO.setId(1L);
        winnerGetDTO.setUsername("Winner");
        given(dtoMapper.convertEntityToUserGetDTO(winner)).willReturn(winnerGetDTO);

        GameController controller = new GameController(gameService, userService);

        UserGetDTO result = controller.winner(gameId);

        assertEquals(winnerGetDTO.getId(), result.getId());
        assertEquals(winnerGetDTO.getUsername(), result.getUsername());
    }

    @Test
    void testWinner_NotFound() throws Exception {
        // HttpStatus 404 Not Found
        when(gameService.getWinner(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/game/1/winner")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

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