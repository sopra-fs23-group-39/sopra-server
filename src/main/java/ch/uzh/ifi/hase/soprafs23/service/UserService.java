package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */

@Service
@Transactional
public class UserService {
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
        newUser.setTotalPointsCurrentGame(0L);
        newUser.setCurrentPoints(0L);
        newUser.setTotalPointsAllGames(0L);
        newUser.setNumberGames(0L);
        newUser.setUserRank(1000L);
        newUser.setBlitzRank(1000L);
        newUser.setTotalBlitzPointsAllGames(0L);
        checkIfUserExists(newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        List<User> allUsersInDB = getUsers();
        updateAllUsersRank(allUsersInDB);
        updateAllBlitzRanks(allUsersInDB);

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
    public void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String message = "The provided username is not unique. Therefore, the user could not be registered!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    message);
        }
    }

    public User getUserProfile(long id) {
        String message = "User with id %d was not found!";
        User userToReturn = userRepository.findById(id);

        if (userToReturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(message, id));
        }

        return userToReturn;
    }

    public User logIn(User userLogin) {

        String userInputUsername = userLogin.getUsername();
        String userInputPassword = userLogin.getPassword();

        User userByUsername = userRepository.findByUsername(userInputUsername);

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You have to enter a username");
        }

        String existingPassword = userByUsername.getPassword();

        userRepository.save(userByUsername);
        userRepository.flush();

        if (userByUsername.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username is wrong or does not exist");
        }
        else if (!(existingPassword.equals(userInputPassword))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong password");
        }

        userByUsername.setStatus(UserStatus.ONLINE);

        return userByUsername;
    }

    public void updateUserProfile(UserPutDTO userPutDTO, long id) {
        String messageId = "User with id %d was not found!";
        User userToUpdate = userRepository.findById(id);

        if (userToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(messageId, id));
        }

        if (userPutDTO.getUsername() != null) {
            userToUpdate.setUsername(userPutDTO.getUsername());
        }

        if (userPutDTO.getPassword() != null) {
            userToUpdate.setPassword(userPutDTO.getPassword());
        }

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

    public void score(AnswerPostDTO answer, long gameId) {
        long score = returnScore(answer, gameId);
        Long userId = answer.getUserId();
        User userById = getUserById(userId);
        Long prevScore = userById.getTotalPointsCurrentGame();
        Long newScore;
        if (prevScore == null) {
            newScore = score + 0;
        }
        else {
            newScore = score + prevScore;
        }
        userById.setTotalPointsCurrentGame(newScore);
        userById.setCurrentPoints(score);
    }

    public long returnScore(AnswerPostDTO answer, long gameId) {
        Game game = gameRepository.findByGameId(gameId);

        long score = 0;
        String correctAnswer = answer.getCorrectAnswer();
        String userAnswer = answer.getUsersAnswer();
        Date time = answer.getTime();
        Date qTime = answer.getQuestionTime();
        long diff = Math.abs(time.getTime() - qTime.getTime());

        if (userAnswer.equals(correctAnswer)) {
            //Scoring function
            if (game.getGameFormat().equals(GameFormat.CUSTOM)) {

                score = (long) (500 / (Math.log((diff / 1000)) * ((double) diff / 1000) + 1));
            }
            else if (game.getGameFormat().equals(GameFormat.BLITZ)) {
                score = (long) (0.1*diff + 100);
            }
        }
        return score;
    }

    public void updateAllGamesScore(AnswerPostDTO answer, long gameId) {
        Game game = gameRepository.findByGameId(gameId);

        if (game.getGameFormat().equals(GameFormat.CUSTOM)) {
            long score = returnScore(answer, gameId);
            Long userId = answer.getUserId();
            User userById = getUserById(userId);
            Long prevScoreAllGames = userById.getTotalPointsAllGames();
            long newScoreAllGames;
            if (prevScoreAllGames == null) {
                newScoreAllGames = score;
            }
            else {
                newScoreAllGames = score + prevScoreAllGames;
            }
            userById.setTotalPointsAllGames(newScoreAllGames);
        }
    }

    public void updateAllBlitzGamesScore(AnswerPostDTO answer, long gameId) {
        Game game = gameRepository.findByGameId(gameId);

        if (game.getGameFormat().equals(GameFormat.BLITZ)) {
            long score = returnScore(answer, gameId);
            Long userId = answer.getUserId();
            User userById = getUserById(userId);
            Long prevScoreAllGames = userById.getTotalPointsAllGames();
            long newScoreAllGames;
            if (prevScoreAllGames == null) {
                newScoreAllGames = score;
            }
            else {
                newScoreAllGames = score + prevScoreAllGames;
            }
            userById.setTotalBlitzPointsAllGames(newScoreAllGames);
        }
    }

    public List<User> retrieveCurrentRanking(long lobbyId) {
        Game game = gameRepository.findByGameId(lobbyId);
        List<User> users = game.getPlayers();
        users.sort(Comparator.comparing(User::getCurrentPoints).reversed());
        return users;
    }

    public List<User> retrieveTotalRanking(long lobbyId) {
        Game game = gameRepository.findByGameId(lobbyId);
        List<User> users = game.getPlayers();
        users.sort(Comparator.comparing(User::getTotalPointsCurrentGame).reversed());
        return users;
    }

    public List<User> sortAllUsersDescOrder(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = new ArrayList<>(allUsersInDB);
        sortedUsersDesc.sort(Comparator.comparing(User::getTotalPointsAllGames).reversed());
        return sortedUsersDesc;
    }

    public void updateAllUsersRank(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = sortAllUsersDescOrder(allUsersInDB);
        for (int i = 0; i < sortedUsersDesc.size(); i++) {
            sortedUsersDesc.get(i).setUserRank((long) (i + 1));
        }
    }

    public List<User> sortAllBlitzRanksDescOrder(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = new ArrayList<>(allUsersInDB);
        sortedUsersDesc.sort(Comparator.comparing(User::getTotalPointsAllGames).reversed());
        return sortedUsersDesc;
    }

    public void updateAllBlitzRanks(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = sortAllBlitzRanksDescOrder(allUsersInDB);
        for (int i = 0; i < sortedUsersDesc.size(); i++) {
            sortedUsersDesc.get(i).setBlitzRank((long) (i + 1));
        }
    }

    public void rapidScore(AnswerPostDTO answer) {
        long score = returnRapidScore(answer);
        Long userId = answer.getUserId();
        User userById = getUserById(userId);
        Long prevScore = userById.getTotalPointsCurrentGame();
        Long newScore;
        if (prevScore == null) {
            newScore = score + 0;
        }
        else {
            newScore = score + prevScore;
        }
        userById.setTotalPointsCurrentGame(newScore);
        userById.setCurrentPoints(score);
    }

    public long returnRapidScore(AnswerPostDTO answer) {
        long score;
        String correctAnswer = answer.getCorrectAnswer();
        String userAnswer = answer.getUsersAnswer();
        Date time = answer.getTime();
        Date qTime = answer.getQuestionTime();
        long diff = Math.abs(time.getTime() - qTime.getTime());
        if (userAnswer.equals(correctAnswer)) {
            //Scoring function
            score = (long) (500 / (Math.log((diff / 1000)) * ((double) diff / 1000) + 1));
        }
        else if (answer.getUsersAnswer().equals("DEFAULT")) {
            score = -30;
        }
        else {
            score = 0;
        }
        return score;
    }

    public void updateAllRapidGamesScore(AnswerPostDTO answer, Long gameId) {
        Game game = gameRepository.findByGameId(gameId);

        if (game.getGameFormat().equals(GameFormat.BLITZ)) {
            long score = returnRapidScore(answer);
            Long userId = answer.getUserId();
            User userById = getUserById(userId);
            Long prevScoreAllGames = userById.getTotalRapidPointsAllGames();
            long newScoreAllGames;
            if (prevScoreAllGames == null) {
                newScoreAllGames = score;
            }
            else {
                newScoreAllGames = score + prevScoreAllGames;
            }
            userById.setTotalRapidPointsAllGames(newScoreAllGames);
        }
    }

    private List<User> sortAllRapidRanksDescOrder(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = new ArrayList<>(allUsersInDB);
        sortedUsersDesc.sort(Comparator.comparing(User::getTotalRapidPointsAllGames).reversed());
        return sortedUsersDesc;
    }

    public void updateAllRapidRanks(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = sortAllRapidRanksDescOrder(allUsersInDB);
        for (int i = 0; i < sortedUsersDesc.size(); i++) {
            sortedUsersDesc.get(i).setRapidRank((long) (i + 1));
        }
    }
}
