package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.questions.Question;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal Game Representation
 * This class composes the internal representation of the game and defines how
 * the game is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {
    public Game() {
        /**
        This no-argument constructor is required,
        Hibernate use reflection on this constructor to instantiate objects
         */
    }
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(nullable = true)
    private GameMode gameMode;

    @Column(nullable = true)
    private Long hostId;

    @Column
    private int questionAmount;

    @Column
    private int timer;

    @Column(nullable = true)
    private GameFormat gameFormat;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JoinColumn(name = "gameHost_id", referencedColumnName = "id")
    private User host;

    @OneToMany(mappedBy = "game", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<User> players = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
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

    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public void setTimer(int timer){this.timer = timer;}

    public int getTimer(){return this.timer;}

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

    public GameFormat getGameFormat(){return gameFormat;}

    public void setGameFormat(GameFormat format){this.gameFormat = format;}
    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    @Transactional
    public List<Question> getQuestions() {
        return questions;
    }
    @Transactional
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