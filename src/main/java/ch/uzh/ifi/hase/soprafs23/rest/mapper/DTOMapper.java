package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.entity.Answer;
import ch.uzh.ifi.hase.soprafs23.entity.Question;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "userRank", ignore = true)
    @Mapping(target = "numberGames", ignore = true)
    @Mapping(target = "totalPointsCurrentGame", ignore = true)
    @Mapping(target = "totalPointsAllGames", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "hostedGame", ignore = true)
    @Mapping(target = "currentPoints", ignore = true)
    @Mapping(target = "totalBlitzPointsAllGames", ignore = true)
    @Mapping(target = "blitzRank", ignore = true)
    @Mapping(target = "totalRapidPointsAllGames", ignore = true)
    @Mapping(target = "rapidRank", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "numberGames", target = "numberGames")
    @Mapping(source = "totalPointsCurrentGame", target = "totalPointsCurrentGame")
    @Mapping(source = "totalPointsAllGames", target = "totalPointsAllGames")
    @Mapping(source = "userRank", target = "userRank")
    @Mapping(source = "currentPoints", target = "currentPoints")
    @Mapping(source = "totalBlitzPointsAllGames", target = "totalBlitzPointsAllGames")
    @Mapping(source = "blitzRank", target = "blitzRank")
    @Mapping(source = "totalRapidPointsAllGames", target = "totalRapidPointsAllGames")
    @Mapping(source = "rapidRank", target = "rapidRank")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "userRank", ignore = true)
    @Mapping(target = "totalPointsCurrentGame", ignore = true)
    @Mapping(target = "totalPointsAllGames", ignore = true)
    @Mapping(target = "numberGames", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "hostedGame", ignore = true)
    @Mapping(target = "currentPoints", ignore = true)
    @Mapping(target = "totalRapidPointsAllGames", ignore = true)
    @Mapping(target = "rapidRank", ignore = true)
    @Mapping(target = "totalBlitzPointsAllGames", ignore = true)
    @Mapping(target = "blitzRank", ignore = true)
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "hostId", target = "hostId")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "questionAmount", target = "questionAmount")
    @Mapping(source = "timer", target = "timer")
    @Mapping(source = "currentRound", target = "currentRound")
    @Mapping(source = "gameFormat", target = "gameFormat")
    @Mapping(source = "isStarted", target = "isStarted")
    GamePostDTO convertEntityToGamePostDTO(Game game);

    @Mapping(source = "questionText", target = "questionText")
    @Mapping(source = "questionLink", target = "questionLink")
    @Mapping(source = "correctAnswer", target = "correctAnswer")
    @Mapping(source = "answer1", target = "answer1")
    @Mapping(source = "answer2", target = "answer2")
    @Mapping(source = "answer3", target = "answer3")
    @Mapping(source = "answer4", target = "answer4")
    @Mapping(source = "creationTime", target = "creationTime")
    QuestionGetDTO convertEntityToQuestionGetDTO(Question question);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "correctAnswer", target = "correctAnswer")
    @Mapping(source = "usersAnswer", target = "usersAnswer")
    @Mapping(source = "time", target = "time")
    @Mapping(source = "questionTime", target = "questionTime")
    Answer convertAnswerPostDTOToEntity(AnswerPostDTO answerPostDTO);
}
