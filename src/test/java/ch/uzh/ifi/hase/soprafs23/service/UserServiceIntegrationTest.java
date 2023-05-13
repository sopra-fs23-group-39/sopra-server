

package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_validInputs_success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setPassword("testPassword");
        testUser2.setUsername("testUsername");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }


/*
    @Test
    public void testLogoutUser() {
        assertNull(userRepository.findById(1L));

        User loggedinUser = new User();
        loggedinUser.setId(1L);
        loggedinUser.setUsername("testUsername");
        loggedinUser.setPassword("testPassword");
        loggedinUser.setStatus(UserStatus.ONLINE);

        userService.createUser(loggedinUser);

        userService.logoutUser(1L);

        User updatedUser = userRepository.findById(1L);

        assertEquals(UserStatus.OFFLINE, updatedUser.getStatus());
    }

     */


    @Test
    public void testLogoutUserWithInvalidInput() {
        User loggedinUser = new User();
        loggedinUser.setId(1L);
        loggedinUser.setUsername("testUsername");
        loggedinUser.setPassword("testPassword");
        loggedinUser.setStatus(UserStatus.ONLINE);

        userService.createUser(loggedinUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.logoutUser(2L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Can't find user to log out", exception.getReason());
    }

    @Test
    public void testCheckIfUserExists() {
        User existingUser = new User();
        existingUser.setUsername("testUsername");
        existingUser.setPassword("testPassword");

        userService.createUser(existingUser);

        // expect ResponseStatusException 409 Conflict
        assertThrows(ResponseStatusException.class, () -> userService.checkIfUserExists(existingUser));
    }

    @Test
    public void testCheckIfUserExistsWithNotExistingUser() {
        User existingUser = new User();
        existingUser.setUsername("testUsername");
        existingUser.setPassword("testPassword");

        // expect no exception
        assertDoesNotThrow(() -> userService.checkIfUserExists(existingUser));
    }


/*
    @Test
    public void testGetUserProfileWithValidId() {
        User getProfileUser = new User();
        getProfileUser.setId(1L);
        getProfileUser.setUsername("testUsername");
        getProfileUser.setPassword("testPassword");

        userService.createUser(getProfileUser);
        userRepository.save(getProfileUser);


        User comparisonUser = userService.getUserProfile(1L);

        // compare user with comparisonUser
        assertEquals(getProfileUser.getId(), comparisonUser.getId());
        assertEquals(getProfileUser.getPassword(), comparisonUser.getPassword());
        assertEquals(getProfileUser.getUsername(), comparisonUser.getUsername());
    }
     */


    @Test
    public void testGetUserProfileWithInvalidId() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        assertThrows(ResponseStatusException.class, () -> userService.getUserProfile(2L));
    }

    @Test
    public void testLogInWithValidCredentials() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        User loggedInUser = userService.logIn(user);

        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }

    @Test
    public void testLogInWithInvalidPassword() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        User userWithInvalidPassword = new User();
        userWithInvalidPassword.setUsername("testUsername");
        userWithInvalidPassword.setPassword("invalidPassword");

        assertThrows(ResponseStatusException.class, () -> userService.logIn(userWithInvalidPassword));
    }

    @Test
    public void testLogInWithoutExistingUsername() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        User userWithNonExistingUsername = new User();
        userWithNonExistingUsername.setUsername("notExistingUsername");
        userWithNonExistingUsername.setPassword("testPassword");

        assertThrows(ResponseStatusException.class, () -> userService.logIn(userWithNonExistingUsername));
    }

    @Test
    public void testUpdateUserProfileWithValidInput() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");
        userPutDTO.setPassword("newPassword");

        userService.updateUserProfile(userPutDTO, user.getId());

        User updatedUser = userService.getUserProfile(user.getId());
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    public void testUpdateUserProfileWithInvalidUsername() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(null);
        userPutDTO.setPassword("newPassword");

        userService.updateUserProfile(userPutDTO, user.getId());

        User updatedUser = userService.getUserProfile(user.getId());
        assertEquals("testUsername", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    public void testUpdateUserProfileWithInvalidPassword() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newUsername");
        userPutDTO.setPassword(null);

        userService.updateUserProfile(userPutDTO, user.getId());

        User updatedUser = userService.getUserProfile(user.getId());
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("testPassword", updatedUser.getPassword());
    }

    @Test
    public void testGetUserById() {
        User newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("testPassword");

        userService.createUser(newUser);

        // get the user from the database
        User obtainedUser = userService.getUserById(newUser.getId());

        // check that the fetched user matches the created user
        assertEquals(newUser.getId(), obtainedUser.getId());
        assertEquals(newUser.getUsername(), obtainedUser.getUsername());
        assertEquals(newUser.getPassword(), obtainedUser.getPassword());
    }

    @Test
    public void testGetUserByInvalidId() {
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(2L));
    }

    @Test
    public void testReadyUserSuccess() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        userService.createUser(user);

        userService.readyUser(user.getId());

        User updatedUser = userService.getUserById(user.getId());
        assertTrue(updatedUser.getIsReady());
    }

    @Test
    public void testReadyUserWhenUserIsNull() {
        assertThrows(ResponseStatusException.class, () -> userService.readyUser(123L));
    }
}

