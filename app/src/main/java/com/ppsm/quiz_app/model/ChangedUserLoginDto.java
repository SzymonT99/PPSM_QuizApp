package com.ppsm.quiz_app.model;

public class ChangedUserLoginDto {

    private String oldLogin;
    private String newLogin;
    private String password;

    public ChangedUserLoginDto() {
    }

    public ChangedUserLoginDto(String oldLogin, String newLogin, String password) {
        this.oldLogin = oldLogin;
        this.newLogin = newLogin;
        this.password = password;
    }

    public String getOldLogin() {
        return oldLogin;
    }

    public void setOldLogin(String oldLogin) {
        this.oldLogin = oldLogin;
    }

    public String getNewLogin() {
        return newLogin;
    }

    public void setNewLogin(String newLogin) {
        this.newLogin = newLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
