package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

public class AnswerPostDTO {

    private String gameId;

    private String userId;

    private String correctAnswer;

    private String usersAnswer;

    private Date time;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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