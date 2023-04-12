package ch.uzh.ifi.hase.soprafs23.questions;

import java.util.ArrayList;

public class Question {

    private String questionText;

    private String questionLink;

    private String correctAnswer;

    private ArrayList<String> wrongAnswers;

    public Question(String questionText, String questionLink, String correctAnswer, ArrayList<String> wrongAnswers) {
        this.questionText = questionText;
        this.questionLink = questionLink;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setId(String questionText) {
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

    public ArrayList<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(ArrayList<String> getWrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}