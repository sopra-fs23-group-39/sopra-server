package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

    @Column
    private int questionAmount;

    @OneToOne
    @JoinColumn(name = "gameHost_id", referencedColumnName = "id")
    private User host;

    //not sure if this works yet..
    @ElementCollection
    @CollectionTable(name = "test", joinColumns = @JoinColumn(name = "test2"))
    @Column(name = "tes3")
    private List<Long> userIds = new ArrayList<>();

    @OneToMany(mappedBy = "game", orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<User> players = new ArrayList<>();

    @ElementCollection
    private List<Question> questions = new ArrayList<>();

    @Column
    private int currentRound;

    public List<User> getPlayers() {
        return players;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    //TODO: add more values here later, this is just for opening a game page with an ID so far..
    public int getQuestionAmount() {
    return questionAmount;
}

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}