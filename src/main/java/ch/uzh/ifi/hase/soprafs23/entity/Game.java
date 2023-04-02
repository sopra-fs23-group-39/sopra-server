package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = true)
    private GameMode gameMode;

    @Column(nullable = true)
    private Long hostId;

//not sure if this works yet..
    @ElementCollection
    @CollectionTable(name = "GAME_USER", joinColumns = @JoinColumn(name = "GAME_ID"))
    @Column(name = "USER_ID")
    private List<Long> userIds = new ArrayList<>();

    //TODO: add more values here later, this is just for opening a game page with an ID so far..

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setParticipants(List<Long> userIds) {
        this.userIds = userIds;
    }
}