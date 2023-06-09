package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

public class QuestionGetDTO {

    private String questionText;

    private String questionLink;

    private String correctAnswer;

    private String answer1;

    private String answer2;

    private String answer3;

    private String answer4;

    private Date creationTime;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionLink() {
        return questionLink;
    }

    public void setQuestionLink(String questionLink) {
        this.questionLink = questionLink;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) { this.answer1 = answer1; }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) { this.answer2 = answer2; }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) { this.answer3 = answer3; }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) { this.answer4 = answer4; }

    public Date getCreationTime() {return creationTime;}

    public void setCreationTime(Date creationTime) {this.creationTime=creationTime;}
}
