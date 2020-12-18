package com.ppsm.quiz_app.model;

import java.util.List;

public class Question {

    private Long id;
    private String content;
    private String category;
    private List<Answer> answers;
    private Integer seconds;
    private boolean available;
    private Integer points;

    public Question() {
    }

    public Question(Long id, String content, String category, List<Answer> answers, Integer seconds, boolean available, Integer points) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.answers = answers;
        this.seconds = seconds;
        this.available = available;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
