package com.ppsm.quiz_app.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private Long id;
    private String content;
    private String category;
    private List<Answer> answers;
    private Integer seconds;
    private boolean available;
    private Integer points;
    private String author;

    public Question() {
    }

    public Question(Long id, String content, String category, List<Answer> answers, Integer seconds, boolean available, Integer points, String author) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.answers = answers;
        this.seconds = seconds;
        this.available = available;
        this.points = points;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
