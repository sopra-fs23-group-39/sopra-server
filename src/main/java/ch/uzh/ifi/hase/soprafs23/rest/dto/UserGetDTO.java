package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;

public class UserGetDTO {
    private Long id;
    private String password;
    private String username;
    private UserStatus status;
    private Long userRank;

    private Long blitzRank;

    //private Long rapidRank;
    private Long numberGames;
    private Long totalPointsCurrentGame;
    private Long currentPoints;
    private Long totalPointsAllGames;
    private boolean isReady;
    private Game game;
    private Game hostedGame;

    private Long totalBlitzPointsAllGames;

    //private Long totalRapidPointsAllGames;

    private Long gameId;

    private Long hostedGameId;

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

    public Long getUserRank() {
    return userRank;
    }

    public void setUserRank(Long rank) {
    this.userRank = rank;
    }

    public Long getBlitzRank(){return blitzRank;}

    public void setBlitzRank(Long blitzRank){this.blitzRank = blitzRank;}

    public Long getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(Long numberGames) {
        this.numberGames = numberGames;
    }

    public Long getTotalPointsCurrentGame() {
        return totalPointsCurrentGame;
    }

    public void setTotalPointsCurrentGame(Long totalPointsCurrentGame) {
        this.totalPointsCurrentGame = totalPointsCurrentGame;
    }


    public UserStatus getStatus() {
    return status;
    }

    public void setStatus(UserStatus status) {
    this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public long getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Long currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Long getTotalPointsAllGames() { return totalPointsAllGames; }

    public void setTotalPointsAllGames(Long totalPointsAllGames) {
        this.totalPointsAllGames = totalPointsAllGames;
    }

    public Long getTotalBlitzPointsAllGames(){return totalBlitzPointsAllGames;}

    public void  setTotalBlitzPointsAllGames(Long totalBlitzPointsAllGames){
        this.totalBlitzPointsAllGames = totalBlitzPointsAllGames;
    }

    /*public Long getRapidRank() { return rapidRank; }

    public void setRapidRank(Long rapidRank) { this.rapidRank = rapidRank; }

    public Long getTotalRapidPointsAllGames() { return totalRapidPointsAllGames; }

    public void setTotalRapidPointsAllGames(Long totalRapidPointsAllGames) { this.totalRapidPointsAllGames = totalRapidPointsAllGames;}*/
}
