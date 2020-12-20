package com.ppsm.quiz_app.model;

public class CreateQuestionDto {

    private String login;
    private String content;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private Integer seconds;
    private Integer correctAnswer;

    public CreateQuestionDto() {
    }

    public CreateQuestionDto(String login, String content, String answerA, String answerB, String answerC, String answerD, Integer seconds, Integer correctAnswer) {
        this.login = login;
        this.content = content;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.seconds = seconds;
        this.correctAnswer = correctAnswer;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
