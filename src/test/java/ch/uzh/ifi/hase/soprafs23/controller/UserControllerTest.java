package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
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

    @MockBean
    private DTOMapper dtoMapper;

//    @Test
//    public void testGetAllUsers() throws Exception {
//        //given
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("Username");
//        user.setPassword("Password");
//        user.setStatus(UserStatus.ONLINE);
//
//        List<User> allUsers = Collections.singletonList(user);
//
//        // this mocks the UserService -> we define what the userService should
//        // return when getUsers() is called
//        given(userService.getUsers()).willReturn(allUsers);
//
//        // when
//        MockHttpServletRequestBuilder getRequest = get("/users")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        // then
//        mockMvc.perform(getRequest).andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
//                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
//                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
//    }

//    @Test
//    public void testGetAllUsers() throws Exception {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setUsername("user2");
//        List<User> users = Arrays.asList(user1, user2);
//
//        given(userService.getUsers()).willReturn(users);
//
//        UserGetDTO userGetDTO1 = new UserGetDTO();
//        userGetDTO1.setId(user1.getId());
//        userGetDTO1.setUsername(user1.getUsername());
//        UserGetDTO userGetDTO2 = new UserGetDTO();
//        userGetDTO2.setId(user2.getId());
//        userGetDTO2.setUsername(user2.getUsername());
//        given(dtoMapper.convertEntityToUserGetDTO(user1)).willReturn(userGetDTO1);
//        given(dtoMapper.convertEntityToUserGetDTO(user2)).willReturn(userGetDTO2);
//
//        MvcResult result = mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        List<UserGetDTO> returnedUsers = objectMapper.readValue(response, new TypeReference<List<UserGetDTO>>() {});
//
//        assertThat(returnedUsers).hasSize(2);
//        assertThat(returnedUsers.get(0).getId()).isEqualTo(user1.getId());
//        assertThat(returnedUsers.get(0).getUsername()).isEqualTo(user1.getUsername());
//        assertThat(returnedUsers.get(1).getId()).isEqualTo(user2.getId());
//        assertThat(returnedUsers.get(1).getUsername()).isEqualTo(user2.getUsername());
//    }

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

        // HttpStatus 201 Created
        given(userService.createUser(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(status().isCreated());
    }

    @Test
    void usersPOST_addInvalidUser() throws Exception {
        User user = new User();
        user.setUsername("testUsername");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        // HttpStatus 409 Conflict
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

        // HttpStatus 200 OK
        given(userService.getUserById(1L)).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    void usersGET_getInvalidUser() throws Exception {
        // HttpStatus 404 Not Found
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

        // HttpStatus 204 No Content
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

        // Use doThrow() to throw an exception when updateUserProfile() is called
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
        assertEquals(result.getPassword(), user.getPassword());
    }

    @Test
    void logInPOST_invalidUsername() throws Exception {
        User user = new User();
        user.setUsername("testUsername");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");

        // HttpStatus 404 Not found
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

        // HttpStatus Unauthorized
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

        // HttpStatus 204 No Content
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

        // Use doThrow() to throw an exception when logoutUser() is called
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