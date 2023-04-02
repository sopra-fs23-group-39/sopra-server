package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.Column;

public class UserGetDTO {

  private Long id;
  private String password;
  private String username;
  private UserStatus status;

    private Long rank;

    private Long numberGames;

    private Long totalPoints;

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

}
