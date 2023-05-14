package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.GameFormat;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.List;

public class GamePostDTO {
    private long gameId;
    private GameMode gameMode;

    private GameFormat gameFormat;
    private Long hostId;
    private int questionAmount;
    private User host;
    private List<User> players;
    private int timer;

    private int currentRound;

    private boolean isStarted;


    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameFormat getGameFormat() {return gameFormat;}

    public void setGameFormat(GameFormat gameFormat) {this.gameFormat = gameFormat;}

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }
    public int getTimer() {return timer;}
    public void setTimer(int timer) {this.timer = timer;}

    public int getCurrentRound() {return currentRound;}

    public void setCurrentRound(int currentRound) {this.currentRound = currentRound;}

    public boolean getIsStarted() { return isStarted;}

    public void setIsStarted(boolean isStarted) {this.isStarted = isStarted;}
}
