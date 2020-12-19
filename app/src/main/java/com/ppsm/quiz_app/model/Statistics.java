package com.ppsm.quiz_app.model;


public class Statistics {

    private Long id;
    private Integer gamesNumber;
    private Integer correctAnswer;
    private Integer incorrectAnswer;
    private Integer addedQuestions;
    private Integer currentRank;

    public Statistics() {
    }

    public Statistics(Long id, Integer gamesNumber, Integer correctAnswer, Integer incorrectAnswer, Integer addedQuestions, Integer currentRank) {
        this.id = id;
        this.gamesNumber = gamesNumber;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer = incorrectAnswer;
        this.addedQuestions = addedQuestions;
        this.currentRank = currentRank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGamesNumber() {
        return gamesNumber;
    }

    public void setGamesNumber(Integer gamesNumber) {
        this.gamesNumber = gamesNumber;
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

    public Integer getAddedQuestions() {
        return addedQuestions;
    }

    public void setAddedQuestions(Integer addedQuestions) {
        this.addedQuestions = addedQuestions;
    }

    public Integer getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Integer currentRank) {
        this.currentRank = currentRank;
    }

}
