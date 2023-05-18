 package ch.uzh.ifi.hase.soprafs23.service;

 import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
 import ch.uzh.ifi.hase.soprafs23.entity.User;
 import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
 import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.Mockito;
 import org.mockito.MockitoAnnotations;
 import org.springframework.web.server.ResponseStatusException;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

 class UserServiceTest {
     @Mock
     private UserRepository userRepository;

     @InjectMocks
     private UserService userService;

     private User testUser;

     @BeforeEach
     public void setup() {
         MockitoAnnotations.openMocks(this);

         // given
         testUser = new User();
         testUser.setId(1L);
         testUser.setPassword("testPassword");
         testUser.setUsername("testUsername");

         // when -> any object is being saved in the userRepository -> return the dummy
         // testUser
         when(userRepository.save(Mockito.any())).thenReturn(testUser);
     }

     @Test
     void createUser_validInputs_success() {
         // when -> any object is being saved in the userRepository -> return the dummy
         // testUser
         User createdUser = userService.createUser(testUser);

         // then
         verify(userRepository, times(1)).save(Mockito.any());

         assertEquals(testUser.getId(), createdUser.getId());
         assertEquals(testUser.getPassword(), createdUser.getPassword());
         assertEquals(testUser.getUsername(), createdUser.getUsername());
         assertNotNull(createdUser.getToken());
         assertEquals(UserStatus.ONLINE, createdUser.getStatus());
         assertNotNull(createdUser.getTotalPointsCurrentGame());
         assertNotNull(createdUser.getCurrentPoints());
         assertNotNull(createdUser.getTotalPointsAllGames());
         assertNotNull(createdUser.getNumberGames());
         assertNotNull(createdUser.getUserRank());
         assertNotNull(createdUser.getBlitzRank());
         assertNotNull(createdUser.getTotalBlitzPointsAllGames());
         assertNotNull(createdUser.getRapidRank());
         assertNotNull(createdUser.getTotalRapidPointsAllGames());
     }

     @Test
     void createUser_duplicateName_throwsException() {
         // given -> a first user has already been created
         userService.createUser(testUser);

         // when -> setup additional mocks for UserRepository
         //Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
         when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

         // then -> attempt to create second user with same user -> check that an error
         // is thrown
         assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
     }

     @Test
     public void loginUser_validInputs_success() {
         // given -> an existing user
         User existingUser = new User();
         existingUser.setId(1L);
         existingUser.setPassword("testPassword");
         existingUser.setUsername("testUsername");
         existingUser.setStatus(UserStatus.OFFLINE);

         // when -> the userRepository is queried for a user with a certain username -> return the dummy
         // testUser
         when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

         // when -> attempt to login with valid credentials
         User loggedInUser = userService.logIn(existingUser);

         // then -> check that user status is set to online
         verify(userRepository, times(1)).flush();
         assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
     }

     @Test
     public void loginUser_nonExistentUsername_throwsNotFoundException() {
         // given -> a user with a non-existent username
         User user = new User();
         user.setPassword("testPassword");
         user.setUsername("nonExistentUsername");

         // when -> userRepository is queried for a user with a certain username -> return null
         when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

         // when -> attempt to log in with non-existent username
         assertThrows(ResponseStatusException.class, () -> userService.logIn(user));
     }

     @Test
     public void loginUser_incorrectPassword_throwsUnauthorizedException() {
         // given -> an existing user
         User existingUser = new User();
         existingUser.setId(1L);
         existingUser.setPassword("correctPassword");
         existingUser.setUsername("testUsername");
         existingUser.setStatus(UserStatus.OFFLINE);

         // given -> a user with an incorrect password
         User user = new User();
         user.setPassword("incorrectPassword");
         user.setUsername("testUsername");

         // when -> userRepository is queried for a user with a certain username -> return the existingUser
         when(userRepository.findByUsername(Mockito.any())).thenReturn(existingUser);

         // when -> attempt to log in with incorrect password
         assertThrows(ResponseStatusException.class, () -> userService.logIn(user));
     }

     @Test
     public void testGetUserProfile() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(user);

         // check that the fetched user matches the created user
         assertEquals(user, userService.getUserProfile(1L));
     }

     @Test
     public void testGetUserProfileInvalidId() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(null);

         assertThrows(ResponseStatusException.class, () -> userService.getUserProfile(1L));
     }

//     @Test
//     public void testLogoutUserWithInvalidInput() {
//         User loggedinUser = new User();
//         loggedinUser.setId(1L);
//         loggedinUser.setUsername("testUsername");
//         loggedinUser.setPassword("testPassword");
//         loggedinUser.setStatus(UserStatus.ONLINE);

//         userService.createUser(loggedinUser);

//         ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//             userService.logoutUser(2L);
//         });

//         assertEquals(HttpStatus.CONFLICT, exception.getStatus());
//         assertEquals("Can't find user to log out", exception.getReason());
//     }

//    @Test
//    public void testLogoutUser_UserExists_SuccessfulLogout() {
//        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
//        Long id = testUser.getId();
//
//        // Act
//        userService.logoutUser(id);
//
//        // Assert
//        verify(userRepository, times(1)).findById(id);
//        verify(userRepository, times(1)).save(testUser);
//        verify(userRepository, times(1)).flush();
//        Assertions.assertEquals(UserStatus.OFFLINE, testUser.getStatus());
//    }

     @Test
     public void testCheckIfUserExists_Conflict() {
         User existingUser = new User();
         existingUser.setUsername("testUsername");
         existingUser.setPassword("testPassword");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(existingUser);

         // expect ResponseStatusException 409 Conflict
         assertThrows(ResponseStatusException.class, () -> userService.checkIfUserExists(existingUser));
     }

     @Test
     public void testCheckIfUserExistsWithoutExistingUser() {
         User existingUser = new User();
         existingUser.setUsername("testUsername");
         existingUser.setPassword("testPassword");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

         // expect no exception
         assertDoesNotThrow(() -> userService.checkIfUserExists(existingUser));
     }

     @Test
     public void updateUserProfile_UserNotFound_ThrowsException() {
         // Given
         Long userId = 1L;
         UserPutDTO userPutDTO = new UserPutDTO();

         // When
         when(userRepository.findById(userId)).thenReturn(null);

         // Then
         assertThrows(ResponseStatusException.class, () -> userService.updateUserProfile(userPutDTO, userId));
     }

//     @Test
//     public void testUpdateUserProfileWithValidInput() {
//         User user = new User();
//         user.setUsername("testUsername");
//         user.setPassword("testPassword");

//         userService.createUser(user);

//         UserPutDTO userPutDTO = new UserPutDTO();
//         userPutDTO.setUsername("newUsername");
//         userPutDTO.setPassword("newPassword");

//         userService.updateUserProfile(userPutDTO, user.getId());

//         User updatedUser = userService.getUserProfile(user.getId());
//         assertEquals("newUsername", updatedUser.getUsername());
//         assertEquals("newPassword", updatedUser.getPassword());
//     }

//     @Test
//     public void testUpdateUserProfileWithInvalidPassword() {
//         User user = new User();
//         user.setUsername("testUsername");
//         user.setPassword("testPassword");

//         userService.createUser(user);

//         UserPutDTO userPutDTO = new UserPutDTO();
//         userPutDTO.setUsername("newUsername");
//         userPutDTO.setPassword(null);

//         userService.updateUserProfile(userPutDTO, user.getId());

//         User updatedUser = userService.getUserProfile(user.getId());
//         assertEquals("newUsername", updatedUser.getUsername());
//         assertEquals("testPassword", updatedUser.getPassword());
//     }

     @Test
     public void testGetUserById() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(user);

         // check that the fetched user matches the created user
         assertEquals(user, userService.getUserById(1L));
     }

     @Test
     public void testGetUserByInvalidId() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(null);

         assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
     }
 }

