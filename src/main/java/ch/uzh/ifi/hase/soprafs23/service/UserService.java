package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.questions.Answer;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
/*TODO maybe it makes sense to merge the two services to one "entityService", as I need to couple
   them anyway.. */
@Service
@Transactional
public class UserService {

   // To log for debugging
    private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final GameRepository gameRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository,
                     @Qualifier("gameRepository") GameRepository gameRepository) {
    this.userRepository = userRepository;
    this.gameRepository = gameRepository;
  }


  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setTotalPoints(0L);
    newUser.setNumberGames(0L);
    newUser.setRank(1000L);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

    public void logoutUser(long userID) {
        User newLoggedinUser = userRepository.findById(userID);
        if (newLoggedinUser == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Can't find user to log out");
        }
        userRepository.save(newLoggedinUser);
        userRepository.flush();
    }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
      User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

      String message = "The provided username is not unique. Therefore, the user could not be registered!";
      if (userByUsername != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT,
                  message);
      }
  }

    public User getUserProfile(long id) {
        String message = "User with id %d was not found!";
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(message, id));
        }
    }

    public User logIn(User userLogin) {

        String userInputUsername = userLogin.getUsername();
        String userInputPassword = userLogin.getPassword();

        User userByUsername = userRepository.findByUsername(userInputUsername);
        String existingPassword = userByUsername.getPassword();

        userRepository.save(userByUsername);
        userRepository.flush();

        if (userByUsername.getUsername() == null) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username is wrong or does not exist");
        }
        else if (!(existingPassword.equals(userInputPassword))){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong password");
        }

        userByUsername.setStatus(UserStatus.ONLINE);

        return userByUsername;
    }

    public void updateUserProfile(UserPutDTO userPutDTO, long id) {
        String messageId = "User with id %d was not found!";
        User userToUpdate;

        try {
            userToUpdate = userRepository.findById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(messageId, id));
        }

        if (userPutDTO.getUsername() != null) {
            userToUpdate.setUsername(userPutDTO.getUsername());
        }

        if (userPutDTO.getPassword() != null) {
            userToUpdate.setPassword(userPutDTO.getPassword());
        }

//        if (userPutDTO.getStatus() != null && !userPutDTO.getStatus().isEmpty()) {
//            userToUpdate.setStatus(UserStatus.valueOf(userPutDTO.getStatus()));
//        }

        userRepository.save(userToUpdate);
        userRepository.flush();
    }

    public User getUserById(long userId) {
        User userById = userRepository.findById(userId);
        if (userById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User not found");
        }
        return userById;
    }
    public void readyUser(long userId) {
        User newReadyUser = userRepository.findById(userId);
        if (newReadyUser == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Can't find user to ready");
        }

        newReadyUser.setIsReady(true);
        userRepository.save(newReadyUser);
        userRepository.flush();
    }

    public void Score(Answer answer){
        long score = ReturnScore(answer);
        Long UserId = answer.getUserId();
        User userById = getUserById(UserId);
        Long prev_score = userById.getGameScore();
        Long new_score = score + prev_score;
        userById.setGameScore(new_score);
        userById.setQuestionScore(score);
    }

    public long ReturnScore(Answer answer){
        long score;
        String CorrectAnswer = answer.getCorrectAnswer();
        String UserAnswer = answer.getUsersAnswer();
        Date time = answer.getTime();
        Date qTime = answer.getQuestionTime();
        long diff = Math.abs(time.getTime() - qTime.getTime());
        if(UserAnswer.equals(CorrectAnswer)){
            score = (long) (500/Math.pow((diff/1000),2));
        }
        else{
            score = 0;
        }
        return score;
    }
}
