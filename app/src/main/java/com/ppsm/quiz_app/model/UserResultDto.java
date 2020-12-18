package com.ppsm.quiz_app.model;

import java.util.Date;

public class UserResultDto {

    private String login;
    private Integer points;
    private Integer correctAnswer;
    private Integer incorrectAnswer;

    public UserResultDto() {
    }

    public UserResultDto(String login, Integer points, Integer correctAnswer, Integer incorrectAnswer) {
        this.login = login;
        this.points = points;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer = incorrectAnswer;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getIncorrectAnswer() {
        return incorrectAnswer;
    }

    public void setIncorrectAnswer(Integer incorrectAnswer) {
        this.incorrectAnswer = incorrectAnswer;
    }
}
