package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.Date;

public class Question {
    private final String questionText;
    private final String questionLink;
    private final String correctAnswer;
    private final String answer1;
    private final String answer2;
    private final String answer3;
    private final String answer4;
    private Date creationTime;

    public Question(String questionText, String questionLink, String correctAnswer, String answer1, String answer2, String answer3, String answer4) {
        this.questionText = questionText;
        this.questionLink = questionLink;
        this.correctAnswer = correctAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionLink() {
        return questionLink;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public Date getCreationTime() {return creationTime;}

    public void setCreationTime(Date date) {this.creationTime = date;}

}