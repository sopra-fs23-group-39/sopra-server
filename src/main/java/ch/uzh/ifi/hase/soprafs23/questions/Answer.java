package ch.uzh.ifi.hase.soprafs23.questions;

import java.util.Date;

public class Answer {

    private Long gameId;

    private Long userId;

    private String correctAnswer;

    private String usersAnswer;

    private Date time;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {this.userId = userId;}

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUsersAnswer() {
        return usersAnswer;
    }

    public void setUsersAnswer(String usersAnswer) { this.usersAnswer = usersAnswer; }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) { this.time = time; }
}
