package com.ppsm.quiz_app.model;

public class DeleteUserDto {

    private String login;
    private String password;

    public DeleteUserDto() {
    }

    public DeleteUserDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
