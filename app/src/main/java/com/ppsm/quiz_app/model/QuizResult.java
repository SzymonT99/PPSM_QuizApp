package com.ppsm.quiz_app.model;

import java.util.Date;

public class QuizResult {

    private String userName;
    private Integer points;
    private Date date;

    public QuizResult() {
    }

    public QuizResult(String userName, Integer points, Date date) {
        this.userName = userName;
        this.points = points;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
