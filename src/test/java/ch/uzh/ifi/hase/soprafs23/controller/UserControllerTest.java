package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void getAllUsersReturnsAllUsers() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setUsername("test1");
        user1.setPassword("testPassword");
        user1.setId(1L);
        user1.setStatus(UserStatus.ONLINE);
        user1.setNumberGames(0L);
        user1.setUserRank(1L);
        user1.setCurrentPoints(0L);
        user1.setTotalPointsAllGames(0L);
        user1.setTotalPointsCurrentGame(0L);
        user1.setRapidRank(1L);
        user1.setTotalRapidPointsAllGames(0L);
        user1.setBlitzRank(1L);
        user1.setTotalBlitzPointsAllGames(0L);

        user2.setUsername("test2");
        user2.setPassword("testPassword");
        user2.setId(2L);
        user2.setStatus(UserStatus.ONLINE);
        user2.setNumberGames(0L);
        user2.setUserRank(1L);
        user2.setCurrentPoints(0L);
        user2.setTotalPointsAllGames(0L);
        user2.setTotalPointsCurrentGame(0L);
        user2.setRapidRank(1L);
        user2.setTotalRapidPointsAllGames(0L);
        user2.setBlitzRank(1L);
        user2.setTotalBlitzPointsAllGames(0L);

        List<User> allUsers = Arrays.asList(user1, user2);
        for(User user: allUsers){
            System.out.println(user.getUsername());
        }
        when(userService.getUsers()).thenReturn(allUsers);
        List<User> someList = userService.getUsers();


        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())))
                .andExpect(status().isOk());

    }

    @Test
     void usersPOST_addValidUser() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setId(1L);
        user.setStatus(UserStatus.ONLINE);
        user.setNumberGames(0L);
        user.setUserRank(1L);
        user.setCurrentPoints(0L);
        user.setTotalPointsAllGames(0L);
        user.setTotalPointsCurrentGame(0L);
        user.setRapidRank(1L);
        user.setTotalRapidPointsAllGames(0L);
        user.setBlitzRank(1L);
        user.setTotalBlitzPointsAllGames(0L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testPassword");

        given(userService.createUser(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(status().isCreated());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    void usersPOST_addInvalidUser() throws Exception {
        User user = new User();
        user.setUsername("testUsername");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        when(userService.createUser(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    void usersGET_getValidUser() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);

        given(userService.getUserById(1L)).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    void usersGET_getInvalidUser() throws Exception {
        when(userService.getUserProfile(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/users/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void usersPUT_putValidUser() throws Exception {
        User user = new User();
        user.setId(1L);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("testUsername");

        Mockito.doNothing().when(userService).updateUserProfile(userPutDTO, 1L);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void usersPUT_putInvalidUser() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("testUsername");

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NO_CONTENT)).when(userService).updateUserProfile(userPutDTO, 1);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void logInPOST_valid() {
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("TestPassword");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("TestUser");
        userPostDTO.setPassword("TestPassword");

        given(userService.logIn(Mockito.any())).willReturn(user);

        UserController userController = new UserController(userService);
        UserGetDTO result = userController.logInUser(userPostDTO);

        assertEquals(result.getUsername(), user.getUsername());
        assertEquals("TestPassword", user.getPassword());
    }

    @Test
    void logInPOST_invalidUsername() throws Exception {
        User user = new User();
        user.setUsername("testUsername");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        when(userService.logIn(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    void logInPOST_invalidPassword() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        when(userService.logIn(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutPUT_putValidUser() throws Exception {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);

        Mockito.doNothing().when(userService).logoutUser(userId);

        MockHttpServletRequestBuilder putRequest = put("/users/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(userId));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void logoutPUT_putInvalidUser() throws Exception {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).logoutUser(userId);

        MockHttpServletRequestBuilder putRequest = put("/users/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(userId));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}