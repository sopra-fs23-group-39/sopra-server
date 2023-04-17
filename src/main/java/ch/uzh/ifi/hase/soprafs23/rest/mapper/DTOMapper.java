package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameCreationDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
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
    @Mapping(target = "rank", ignore = true)
    @Mapping(target = "numberGames", ignore = true)
    @Mapping(target = "totalPoints", ignore = true)
    @Mapping(target = "isReady", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "numberGames", target = "numberGames")
    @Mapping(source = "totalPoints", target = "totalPoints")
    @Mapping(source = "rank", target = "rank")
    @Mapping(source = "isReady", target = "isReady")
    @Mapping(source = "game", target = "game")
    @Mapping(source = "hostedGame", target = "hostedGame")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "rank", ignore = true)
    @Mapping(target = "totalPoints", ignore = true)
    @Mapping(target = "numberGames", ignore = true)
    @Mapping(source = "isReady", target = "isReady")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);



    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "hostId", target = "hostId")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "questionAmount", target = "questionAmount")
    @Mapping(source = "timer", target = "timer")
    GameCreationDTO convertEntityToGameCreationDTO(Game game);
}
