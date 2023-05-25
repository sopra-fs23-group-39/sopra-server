 package ch.uzh.ifi.hase.soprafs23.service;

 import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
 import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
 import ch.uzh.ifi.hase.soprafs23.entity.Game;
 import ch.uzh.ifi.hase.soprafs23.entity.User;
 import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
 import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
 import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
 import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.Mockito;
 import org.mockito.MockitoAnnotations;
 import org.springframework.http.HttpStatus;
 import org.springframework.web.server.ResponseStatusException;

 import java.time.LocalDateTime;
 import java.time.ZoneId;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.Date;
 import java.util.List;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

 class UserServiceTest {
     @Mock
     private UserRepository userRepository;

     @Mock
     private GameRepository gameRepository;

     @InjectMocks
     private UserService userService;

     private User testUser;

     private final AnswerPostDTO answerPostDTO = new AnswerPostDTO();

     @BeforeEach
     void setup() {
         MockitoAnnotations.openMocks(this);

         testUser = new User();
         testUser.setId(1L);
         testUser.setPassword("testPassword");
         testUser.setUsername("testUsername");

         when(userRepository.save(Mockito.any())).thenReturn(testUser);
     }

     @Test
     void getUsersTest() {
         List<User> userList = new ArrayList<>();
         userList.add(testUser);

         when(userRepository.findAll()).thenReturn(userList);
         List<User> users = userService.getUsers();

         assertEquals(1, users.size());
         assertEquals(testUser.getUsername(), users.get(0).getUsername());
         assertEquals(testUser.getPassword(), users.get(0).getPassword());
     }

     @Test
     void createUser_validInputs_success() {
         User createdUser = userService.createUser(testUser);

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
         userService.createUser(testUser);

         when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

         assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
     }

     @Test
     void loginUser_validInputs_success() {
         User existingUser = new User();
         existingUser.setId(1L);
         existingUser.setPassword("testPassword");
         existingUser.setUsername("testUsername");
         existingUser.setStatus(UserStatus.OFFLINE);

         when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

         User loggedInUser = userService.logIn(existingUser);

         verify(userRepository, times(1)).flush();
         assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
     }

     @Test
     void loginUser_nonExistentUsername_throwsNotFoundException() {
         User user = new User();
         user.setPassword("testPassword");
         user.setUsername("nonExistentUsername");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

         assertThrows(ResponseStatusException.class, () -> userService.logIn(user));
     }

     @Test
     void loginUser_incorrectPassword_throwsUnauthorizedException() {
         User existingUser = new User();
         existingUser.setId(1L);
         existingUser.setPassword("correctPassword");
         existingUser.setUsername("testUsername");
         existingUser.setStatus(UserStatus.OFFLINE);

         User user = new User();
         user.setPassword("incorrectPassword");
         user.setUsername("testUsername");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(existingUser);

         assertThrows(ResponseStatusException.class, () -> userService.logIn(user));
     }

     @Test
     void testGetUserProfile() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(user);

         assertEquals(user, userService.getUserProfile(1L));
     }

     @Test
     void testGetUserProfileInvalidId() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(null);

         assertThrows(ResponseStatusException.class, () -> userService.getUserProfile(1L));
     }

     @Test
     void testLogoutUser() {
         long id = 1;
         testUser = new User();
         testUser.setId(id);
         testUser.setPassword("testPassword");
         testUser.setUsername("testUsername");
         when(userRepository.findById(id)).thenReturn(testUser);
         userService.logoutUser(id);
         verify(userRepository).save(testUser);
         assertEquals(UserStatus.OFFLINE, testUser.getStatus());
     }

     @Test
     void testLogoutUser_NotFound() {
         long id = 1;
         when(userRepository.findById(id)).thenReturn(null);
         assertThrows(ResponseStatusException.class, () -> userService.logoutUser(id));
     }

     @Test
     void testCheckIfUserExists_Conflict() {
         User existingUser = new User();
         existingUser.setUsername("testUsername");
         existingUser.setPassword("testPassword");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(existingUser);

         assertThrows(ResponseStatusException.class, () -> userService.checkIfUserExists(existingUser));
     }

     @Test
     void testCheckIfUserExistsWithoutExistingUser() {
         User existingUser = new User();
         existingUser.setUsername("testUsername");
         existingUser.setPassword("testPassword");

         when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

         assertDoesNotThrow(() -> userService.checkIfUserExists(existingUser));
     }

    @Test
    void updateUserProfile_NotFound_invalidUserId() {
        long userId = 1L;
        String username = "Alice";

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(userId);
        userPutDTO.setUsername(username);

        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.updateUserProfile(userPutDTO, userId));

        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

     @Test
     void updateUserProfile_Conflict_userWithThisUsernameAlreadyExistsAndIsDifferentUser() {
         long userId = 1L;
         String username = "Alice";
         UserPutDTO userPutDTO = new UserPutDTO();
         userPutDTO.setId(userId);
         userPutDTO.setUsername(username);

         User existingUser = new User();
         existingUser.setId(userId);
         existingUser.setUsername("Mary");

         User thirdUser = new User();
         thirdUser.setUsername(username);

         when(userRepository.findById(userId)).thenReturn(existingUser);
         when(userRepository.findByUsername(username)).thenReturn(thirdUser);

         assertThrows(ResponseStatusException.class, () -> userService.updateUserProfile(userPutDTO, userId));

         verify(userRepository, never()).save(any(User.class));
         verify(userRepository, never()).flush();
     }

     @Test
     void updateUserProfile_WithExistingUser_ShouldUpdateUsernameAndPassword() {
         long userId = 1L;
         String newUsername = "newUsername";
         String newPassword = "newPassword";

         UserPutDTO userPutDTO = new UserPutDTO();
         userPutDTO.setUsername(newUsername);
         userPutDTO.setPassword(newPassword);
         userPutDTO.setId(userId);

         User existingUser = new User();
         existingUser.setId(userId);
         existingUser.setUsername("oldUsername");
         existingUser.setPassword("oldPassword");

         Mockito.when(userRepository.findById(userId)).thenReturn(existingUser);
         Mockito.when(userRepository.findByUsername(newUsername)).thenReturn(null);

         userService.updateUserProfile(userPutDTO, userId);

         assertEquals(newUsername, existingUser.getUsername());
         assertEquals(newPassword, existingUser.getPassword());
         verify(userRepository, Mockito.times(1)).save(existingUser);
         verify(userRepository, Mockito.times(1)).flush();
     }

     @Test
     void updateUserProfile_WithExistingUserAndNullNewValues_ShouldNotUpdateUsernameAndPassword() {
         long userId = 1L;
         String newUsername = null;
         String newPassword = null;

         UserPutDTO userPutDTO = new UserPutDTO();
         userPutDTO.setUsername(newUsername);
         userPutDTO.setPassword(newPassword);
         userPutDTO.setId(userId);

         User existingUser = new User();
         existingUser.setId(userId);
         existingUser.setUsername("oldUsername");
         existingUser.setPassword("oldPassword");

         Mockito.when(userRepository.findById(userId)).thenReturn(existingUser);
         Mockito.when(userRepository.findByUsername(newUsername)).thenReturn(null);

         userService.updateUserProfile(userPutDTO, userId);

         assertEquals("oldUsername", existingUser.getUsername());
         assertEquals("oldPassword", existingUser.getPassword());
         verify(userRepository, Mockito.times(1)).save(existingUser);
         verify(userRepository, Mockito.times(1)).flush();
     }

     @Test
     void updateUserProfile_WithExistingUserAndEmptyStrings_ShouldNotUpdateUsernameAndPassword() {
         long userId = 1L;
         String newUsername = "";
         String newPassword = "  ";

         UserPutDTO userPutDTO = new UserPutDTO();
         userPutDTO.setUsername(newUsername);
         userPutDTO.setPassword(newPassword);
         userPutDTO.setId(userId);

         User existingUser = new User();
         existingUser.setId(userId);
         existingUser.setUsername("oldUsername");
         existingUser.setPassword("oldPassword");

         Mockito.when(userRepository.findById(userId)).thenReturn(existingUser);
         Mockito.when(userRepository.findByUsername(newUsername)).thenReturn(null);

         userService.updateUserProfile(userPutDTO, userId);

         assertEquals("oldUsername", existingUser.getUsername());
         assertEquals("oldPassword", existingUser.getPassword());
         verify(userRepository, Mockito.times(1)).save(existingUser);
         verify(userRepository, Mockito.times(1)).flush();
     }

     @Test
     void testGetUserById() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(user);

         assertEquals(user, userService.getUserById(1L));
     }

     @Test
     void testGetUserByInvalidId() {
         Long userId = 1L;
         User user = new User();
         user.setId(userId);

         when(userRepository.findById(1L)).thenReturn(null);

         assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
     }

     @BeforeEach
     void answerSetup() {
         answerPostDTO.setUserId(1L);
         answerPostDTO.setGameId(1L);
         answerPostDTO.setCorrectAnswer("Inception");

         LocalDateTime questionDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(30);
         Date questionTime = Date.from(questionDateTime.atZone(ZoneId.systemDefault()).toInstant());
         answerPostDTO.setQuestionTime(questionTime);

         LocalDateTime answerDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(32);
         Date answerTime = Date.from(answerDateTime.atZone(ZoneId.systemDefault()).toInstant());
         answerPostDTO.setTime(answerTime);

     }
    @Test
    void testScore_CUSTOM_correctAnswer() {
         answerPostDTO.setUsersAnswer("Inception");

        GameFormat gameFormat = GameFormat.CUSTOM;

        User user = new User();
        long userId = 1L;
        user.setId(userId);
        user.setTotalPointsCurrentGame(100L);

        when(userRepository.findById(userId)).thenReturn(user);

        userService.score(answerPostDTO, gameFormat);

        assertEquals(298L, user.getTotalPointsCurrentGame());
    }

     @Test
     void testScore_CUSTOM_wrongAnswer() {
         answerPostDTO.setUsersAnswer("Shutter Island");

         GameFormat gameFormat = GameFormat.CUSTOM;

         User user = new User();
         long userId = 1L;
         user.setId(userId);
         user.setTotalPointsCurrentGame(100L);

         when(userRepository.findById(userId)).thenReturn(user);

         userService.score(answerPostDTO, gameFormat);

         assertEquals(100L, user.getTotalPointsCurrentGame());
     }

     @Test
     void testScore_BLITZ_correctAnswer() {
         answerPostDTO.setUsersAnswer("Inception");

         GameFormat gameFormat = GameFormat.BLITZ;

         User user = new User();
         long userId = 1L;
         user.setId(userId);
         user.setTotalPointsCurrentGame(100L);

         when(userRepository.findById(userId)).thenReturn(user);

         userService.score(answerPostDTO, gameFormat);

         assertEquals(298L, user.getTotalPointsCurrentGame());
     }

     @Test
     void testScore_BLITZ_wrongAnswer() {
         answerPostDTO.setUsersAnswer("Shutter Island");

         GameFormat gameFormat = GameFormat.BLITZ;

         User user = new User();
         long userId = 1L;
         user.setId(userId);
         user.setTotalPointsCurrentGame(100L);

         when(userRepository.findById(userId)).thenReturn(user);

         userService.score(answerPostDTO, gameFormat);

         assertEquals(100L, user.getTotalPointsCurrentGame());
     }

     @Test
     void testScore_RAPID_correctAnswer() {
         answerPostDTO.setUsersAnswer("Inception");

         GameFormat gameFormat = GameFormat.RAPID;

         User user = new User();
         long userId = 1L;
         user.setId(userId);
         user.setTotalPointsCurrentGame(100L);

         when(userRepository.findById(userId)).thenReturn(user);

         userService.score(answerPostDTO, gameFormat);

         assertEquals(298L, user.getTotalPointsCurrentGame());
     }

     @Test
     void testScore_RAPID_wrongAnswer() {
         answerPostDTO.setUsersAnswer("Shutter Island");

         GameFormat gameFormat = GameFormat.RAPID;

         User user = new User();
         long userId = 1L;
         user.setId(userId);
         user.setTotalPointsCurrentGame(100L);

         when(userRepository.findById(userId)).thenReturn(user);

         userService.score(answerPostDTO, gameFormat);

         assertEquals(50L, user.getTotalPointsCurrentGame());
     }

     @Test
     void testRetrieveCurrentRanking() {
         long gameId = 1L;
         Game game = new Game();
         List<User> users = new ArrayList<>();
         User user1 = new User();
         user1.setUsername("User1");
         user1.setCurrentPoints(10L);
         User user2 = new User();
         user2.setUsername("User2");
         user2.setCurrentPoints(20L);
         game.setPlayers(users);

         when(gameRepository.findByGameId(gameId)).thenReturn(game);

         List<User> result = userService.retrieveCurrentRanking(gameId);

         List<User> expected = new ArrayList<>(users);
         expected.sort(Comparator.comparing(User::getCurrentPoints).reversed());
         assertEquals(expected, result);
     }

     @Test
     void testRetrieveTotalRanking() {
         long gameId = 1L;
         Game game = new Game();
         List<User> users = new ArrayList<>();
         User user1 = new User();
         user1.setUsername("User1");
         user1.setTotalPointsCurrentGame(10L);
         User user2 = new User();
         user2.setUsername("User2");
         user2.setTotalPointsCurrentGame(20L);
         game.setPlayers(users);

         when(gameRepository.findByGameId(gameId)).thenReturn(game);

         List<User> result = userService.retrieveTotalRanking(gameId);

         List<User> expected = new ArrayList<>(users);
         expected.sort(Comparator.comparing(User::getTotalPointsCurrentGame).reversed());
         assertEquals(expected, result);
     }

     @Test
     void testGetUsers_GameExists() {
         long gameId = 1L;

         Game game = new Game();
         List<User> users = new ArrayList<>();
         users.add(new User());
         users.add(new User());
         game.setPlayers(users);

         when(gameRepository.findByGameId(gameId)).thenReturn(game);

         List<User> result = userService.getGamePlayers(gameId);
         assertEquals(users, result);
     }

     @Test
     void testGetUsers_GameNotFound() {
         long gameId = 1L;

         when(gameRepository.findByGameId(gameId)).thenReturn(null);

         ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                 () -> userService.getGamePlayers(gameId),
                 "No games with id " + gameId + " was found");

         assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
         assertEquals("No games with id " + gameId + " was found", exception.getReason());
     }

     @Test
     void testGetPrevScoreAllGames() {
         User user = new User();
         user.setTotalBlitzPointsAllGames(100L);
         user.setTotalRapidPointsAllGames(200L);
         user.setTotalPointsAllGames(300L);

         Long prevScoreBlitz = userService.getPrevScoreAllGames(GameFormat.BLITZ, user);
         assertEquals(100L, prevScoreBlitz);

         Long prevScoreRapid = userService.getPrevScoreAllGames(GameFormat.RAPID, user);
         assertEquals(200L, prevScoreRapid);

         Long prevScoreDefault = userService.getPrevScoreAllGames(GameFormat.CUSTOM, user);
         assertEquals(300L, prevScoreDefault);
     }

     @Test
     void testUpdateAllGamesScoreAuxiliaryMethod_BLITZ() {
         GameFormat gameFormat = GameFormat.BLITZ;
         User user = Mockito.mock(User.class);
         long newScoreAllGames = 100L;

         userService.updateAllGamesScoreAuxiliaryMethod(gameFormat, newScoreAllGames, user);

         verify(user).setTotalBlitzPointsAllGames(newScoreAllGames);
         verify(user, Mockito.never()).setTotalRapidPointsAllGames(Mockito.anyLong());
         verify(user, Mockito.never()).setTotalPointsAllGames(Mockito.anyLong());
     }

     @Test
     void testUpdateAllGamesScoreAuxiliaryMethod_RAPID() {
         GameFormat gameFormat = GameFormat.RAPID;
         User user = Mockito.mock(User.class);
         long newScoreAllGames = 100L;

         userService.updateAllGamesScoreAuxiliaryMethod(gameFormat, newScoreAllGames, user);

         verify(user).setTotalRapidPointsAllGames(newScoreAllGames);
         verify(user, Mockito.never()).setTotalBlitzPointsAllGames(Mockito.anyLong());
         verify(user, Mockito.never()).setTotalPointsAllGames(Mockito.anyLong());
     }

     @Test
     void testUpdateAllGamesScoreAuxiliaryMethod_CUSTOM() {
         GameFormat gameFormat = GameFormat.CUSTOM;
         User user = Mockito.mock(User.class);
         long newScoreAllGames = 100L;

         userService.updateAllGamesScoreAuxiliaryMethod(gameFormat, newScoreAllGames, user);

         verify(user).setTotalPointsAllGames(newScoreAllGames);
         verify(user, Mockito.never()).setTotalBlitzPointsAllGames(Mockito.anyLong());
         verify(user, Mockito.never()).setTotalRapidPointsAllGames(Mockito.anyLong());
     }

     @Test
     void testSetGameAndSetHostedGame() {
         User user = new User();
         Game game = new Game();

         user.setGame(game);
         assertEquals(game, user.getGame());

         user.setHostedGame(game);
         assertEquals(game, user.getHostedGame());
     }

 }

