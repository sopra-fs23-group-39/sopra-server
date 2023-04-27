package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

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
@Table(name = "USER")
public class User implements Serializable {
    public User() {
        /**
         This no-argument constructor is required,
         Hibernate use reflection on this constructor to instantiate objects
         */
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private Long userRank;

    @Column(nullable = false)
    private Long numberGames;

    @Column(nullable = false)
    private Long totalPointsCurrentGame;

    @Column(nullable = false)
    private Long currentPoints;

    @Column(nullable = false)
    private Long totalPointsAllGames;

    @Column(nullable = false)
    private boolean isReady;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
    private Game game;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hosted_game_id", referencedColumnName = "game_id", nullable = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
    private Game hostedGame;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getHostedGame() {
        return hostedGame;
    }

    public void setHostedGame(Game hostedGame) {
        this.hostedGame = hostedGame;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Long getUserRank() {
        return userRank;
    }

    public void setUserRank(Long rank) {
        this.userRank = rank;
    }

    public Long getTotalPointsCurrentGame() {
        return totalPointsCurrentGame;
    }

    public void setTotalPointsCurrentGame(Long totalPoints) {
        this.totalPointsCurrentGame = totalPoints;
    }
    public Long getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Long currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Long getTotalPointsAllGames() { return totalPointsAllGames; }

    public void setTotalPointsAllGames(Long totalPointsAllGames) {
        this.totalPointsAllGames = totalPointsAllGames;
    }

    public Long getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(Long numberGames) {
        this.numberGames = numberGames;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

}
