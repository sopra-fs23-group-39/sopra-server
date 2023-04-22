package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import javax.persistence.Column;

public class UserGetDTO {

  private Long id;
  private String password;
  private String username;
  private UserStatus status;

    private Long rank;

    private Long numberGames;

    private Long totalPoints;

    private boolean isReady;
    private Game game;
    private Game hostedGame;

    private Long gameId;

    private Long hostedGameId;

    public UserGetDTO() {}

    public UserGetDTO(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.status = user.getStatus();
        this.rank = user.getRank();
        this.numberGames = user.getNumberGames();
        this.totalPoints = user.getTotalPoints();
        this.isReady = user.getIsReady();
        if (user.getGame() != null) {
            this.gameId = user.getGame().getGameId();
        }
        if (user.getHostedGame() != null) {
            this.hostedGameId = user.getHostedGame().getGameId();
        }
    }


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

  public Long getRank() {
    return rank;
  }

  public void setRank(Long rank) {
    this.rank = rank;
  }

    public Long getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(Long numberGames) {
        this.numberGames = numberGames;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
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
}
