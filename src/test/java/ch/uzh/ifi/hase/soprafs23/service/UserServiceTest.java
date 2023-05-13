//package ch.uzh.ifi.hase.soprafs23.service;
//
//import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
//import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private GameRepository gameRepository;
//    @InjectMocks
//    private UserService userService = new UserService(userRepository, gameRepository);
//
////    private User testUser = new User();
//    private AnswerPostDTO answerPostDTO = new AnswerPostDTO();
//
////  @BeforeEach
////  public void setup() {
////    MockitoAnnotations.openMocks(this);
////
////    // given
////    testUser = new User();
////    testUser.setId(1L);
////    testUser.setUsername("testUsername");
////
////    // when -> any object is being save in the userRepository -> return the dummy
////    // testUser
////    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
////  }
//
////  @Test
////  public void createUser_validInputs_success() {
////    // when -> any object is being save in the userRepository -> return the dummy
////    // testUser
////    User createdUser = userService.createUser(testUser);
////
////    // then
////    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
////
////    assertEquals(testUser.getId(), createdUser.getId());
////    assertEquals(testUser.getName(), createdUser.getName());
////    assertEquals(testUser.getUsername(), createdUser.getUsername());
////    assertNotNull(createdUser.getToken());
////    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
////  }
////
////  @Test
////  public void createUser_duplicateName_throwsException() {
////    // given -> a first user has already been created
////    userService.createUser(testUser);
////
////    // when -> setup additional mocks for UserRepository
////    Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
////    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
////
////    // then -> attempt to create second user with same user -> check that an error
////    // is thrown
////    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
////  }
////
////  @Test
////  public void createUser_duplicateInputs_throwsException() {
////    // given -> a first user has already been created
////    userService.createUser(testUser);
////
////    // when -> setup additional mocks for UserRepository
////    Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
////    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
////
////    // then -> attempt to create second user with same user -> check that an error
////    // is thrown
////    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
////  }
//
//    @BeforeEach
//    void answerSetup() {
//        answerPostDTO.setUserId(1L);
//        answerPostDTO.setGameId(1L);
//        answerPostDTO.setCorrectAnswer("Inception");
//
//        LocalDateTime questionDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(30);
//        Date questionTime = Date.from(questionDateTime.atZone(ZoneId.systemDefault()).toInstant());
//        answerPostDTO.setQuestionTime(questionTime);
//
//        LocalDateTime answerDateTime = LocalDateTime.of(2023, 5, 13, 10, 30).plusSeconds(32);
//        Date answerTime = Date.from(answerDateTime.atZone(ZoneId.systemDefault()).toInstant());
//        answerPostDTO.setTime(answerTime);
//    }
//
//    @Test
//    void correctScoreCalculation_wrongAnswer() {
//        answerPostDTO.setUsersAnswer("Shutter Island");
//        assertEquals(0, userService.returnScore(answerPostDTO));
//    }
//
//    @Test
//    void correctScoreCalculation_rightAnswer() {
//        answerPostDTO.setUsersAnswer("Inception");
//        assertEquals(209, userService.returnScore(answerPostDTO));
//    }
//
//    @Test
//    void correctScoreCalculationRapid_wrongAnswer() {
//        answerPostDTO.setUsersAnswer("Shutter Island");
//        assertEquals(0, userService.returnRapidScore(answerPostDTO));
//    }
//
//    @Test
//    void correctScoreCalculationRapid_wrongAnswerDefault() {
//        answerPostDTO.setUsersAnswer("DEFAULT");
//        assertEquals(-30, userService.returnRapidScore(answerPostDTO));
//    }
//
//    @Test
//    void correctScoreCalculationRapid_rightAnswer() {
//        answerPostDTO.setUsersAnswer("Inception");
//        assertEquals(userService.returnRapidScore(answerPostDTO),209);
//    }
//
//}
