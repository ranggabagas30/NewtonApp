package com.newtonapp.data.network.pojo.request;

public class ComplainRequestModel {

    private String action;
    private String username;
    private String password;
    private String category;
    private String message;

    public ComplainRequestModel(String action, String username, String password, String category, String message) {
        this.action = action;
        this.username = username;
        this.password = password;
        this.category = category;
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
