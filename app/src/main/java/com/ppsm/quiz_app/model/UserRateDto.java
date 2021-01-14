package com.ppsm.quiz_app.model;

public class UserRateDto {

    private String userName;
    private boolean opinion;

    public UserRateDto() {
    }

    public UserRateDto(String userName, boolean opinion) {
        this.userName = userName;
        this.opinion = opinion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isOpinion() {
        return opinion;
    }

    public void setOpinion(boolean opinion) {
        this.opinion = opinion;
    }
}

