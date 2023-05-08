package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.Embeddable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class Question {
    private String questionText;
    private String questionLink;
    private String correctAnswer;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
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

    public Question() {

    }

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

    public Date getCreationTime(){return creationTime;}

    public void setCreationTime(Date date){this.creationTime = date;}

    @Override
    public boolean equals(Object questionToCompare) {
        if (this == questionToCompare) {
            return true;
        }
        if (questionToCompare == null || getClass() != questionToCompare.getClass()) {
            return false;
        }
        Question other = (Question) questionToCompare;
        return Objects.equals(correctAnswer, other.getCorrectAnswer());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + correctAnswer.hashCode();
        return result;
    }
}