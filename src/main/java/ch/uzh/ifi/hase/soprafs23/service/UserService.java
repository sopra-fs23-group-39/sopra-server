package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
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
        checkIfUserExists(newUser);
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setTotalPointsCurrentGame(0L);
        newUser.setCurrentPoints(0L);
        newUser.setTotalPointsAllGames(0L);
        newUser.setNumberGames(0L);
        newUser.setUserRank(1000L);
        newUser.setBlitzRank(1000L);
        newUser.setTotalBlitzPointsAllGames(0L);
        newUser.setRapidRank(1000L);
        newUser.setTotalRapidPointsAllGames(0L);
        newUser = userRepository.save(newUser);
        userRepository.flush();

        List<User> allUsersInDB = getUsers();
        updateAllUsersRank(allUsersInDB);
        updateAllBlitzRanks(allUsersInDB);
        updateAllRapidRanks(allUsersInDB);

        return newUser;
    }

    public void logoutUser(long userID) {
        User user = userRepository.findById(userID);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find a user to log out");
        }
        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
        userRepository.flush();
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username defined in the User entity. The method will do nothing if the input is unique
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with these credentials is not yet registered. Please, register first!");
        }

        String existingPassword = userByUsername.getPassword();

        if (!(existingPassword.equals(userInputPassword))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The provided password is wrong!");
        }

        userByUsername.setStatus(UserStatus.ONLINE);

        userRepository.save(userByUsername);
        userRepository.flush();

        return userByUsername;
    }

    public void updateUserProfile(UserPutDTO userPutDTO, long id) {
        User userToUpdate = userRepository.findById(id);

        String newUsername = userPutDTO.getUsername();
        String newPassword = userPutDTO.getPassword();
        User userWithTheSameUsername = userRepository.findByUsername(newUsername);

        if (userToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d does not exist!", id));
        } else if (userWithTheSameUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this username already exists, please choose another username!");
        } else {
            String oldUsername = userToUpdate.getUsername();
            String oldPassword = userToUpdate.getPassword();
            if (newUsername == null) {
                userToUpdate.setUsername(oldUsername);
            } else if (newUsername.trim().isEmpty()) {
                userToUpdate.setUsername(oldUsername);
            } else {
                userToUpdate.setUsername(newUsername);
            }

            if (newPassword == null) {
                userToUpdate.setPassword(oldPassword);
            } else if (newPassword.trim().isEmpty()) {
                userToUpdate.setPassword(oldPassword);
            } else {
                userToUpdate.setPassword(newPassword);
            }
        }

        userRepository.save(userToUpdate);
        userRepository.flush();
    }

    public User getUserById(long userId) {
        User userById = userRepository.findById(userId);
        if (userById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User with id %d was not found!", userId));
        }
        return userById;
    }

    public void score(AnswerPostDTO answer, GameFormat gameFormat) {
        long score = returnScore(answer, gameFormat);
        Long userId = answer.getUserId();
        User userById = getUserById(userId);
        Long prevScore = userById.getTotalPointsCurrentGame();
        Long newScore = score + prevScore;

        userById.setTotalPointsCurrentGame(newScore);
        userById.setCurrentPoints(score);
    }

    public long returnScore(AnswerPostDTO answer, GameFormat gameFormat) {
        double score;
        double doubleScore;
        String correctAnswer = answer.getCorrectAnswer();
        String userAnswer = answer.getUsersAnswer();
        Date time = answer.getTime();
        Date qTime = answer.getQuestionTime();
        double diff = Math.abs(time.getTime() - qTime.getTime());
        diff = diff/2.5;

        if (userAnswer.equals(correctAnswer)) {
            score =  (500d / ( diff/1000d * Math.log((diff / 1000d)+1d) + 1d));
            }
        else {
            score = switch (gameFormat) {
                case RAPID -> -50;
                default -> 0;
            };
        }
        //score = (long) doubleScore;
        return (long) score;
    }

    public void updateAllGamesScore(AnswerPostDTO answer, GameFormat gameFormat) {
        long score = returnScore(answer, gameFormat);
        Long userId = answer.getUserId();
        User userById = getUserById(userId);
        Long prevScoreAllGames = getPrevScoreAllGames(gameFormat, userById);

        long newScoreAllGames = score + prevScoreAllGames;

        updateAllGamesScoreAuxiliaryMethod(gameFormat, newScoreAllGames, userById);
    }

    public void updateAllGamesScoreAuxiliaryMethod(GameFormat gameFormat, long newScoreAllGames, User user) {
        switch (gameFormat) {
            case BLITZ -> user.setTotalBlitzPointsAllGames(newScoreAllGames);
            case RAPID -> user.setTotalRapidPointsAllGames(newScoreAllGames);
            default -> user.setTotalPointsAllGames(newScoreAllGames);
        }
    }

    Long getPrevScoreAllGames(GameFormat gameFormat, User userById) {
        return switch (gameFormat) {
            case BLITZ -> userById.getTotalBlitzPointsAllGames();
            case RAPID -> userById.getTotalRapidPointsAllGames();
            default -> userById.getTotalPointsAllGames();
        };
    }

    public List<User> retrieveCurrentRanking(long lobbyId) {
        List<User> users = getGamePlayers(lobbyId);
        users.sort(Comparator.comparing(User::getCurrentPoints).reversed());
        return users;
    }

    public List<User> retrieveTotalRanking(long lobbyId) {
        List<User> users = getGamePlayers(lobbyId);
        users.sort(Comparator.comparing(User::getTotalPointsCurrentGame).reversed());
        return users;
    }

    public List<User> getGamePlayers(long lobbyId) {
        Game game = gameRepository.findByGameId(lobbyId);
        String message = String.format("No games with id %d was found", lobbyId);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return game.getPlayers();
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
        sortedUsersDesc.sort(Comparator.comparing(User::getTotalBlitzPointsAllGames).reversed());
        return sortedUsersDesc;
    }

    public void updateAllBlitzRanks(List<User> allUsersInDB) {
        List<User> sortedUsersDesc = sortAllBlitzRanksDescOrder(allUsersInDB);
        for (int i = 0; i < sortedUsersDesc.size(); i++) {
            sortedUsersDesc.get(i).setBlitzRank((long) (i + 1));
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
