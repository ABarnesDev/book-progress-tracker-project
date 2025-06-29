package com.cognixia.fh.exception;

public class UsernameTakenException extends Exception {
    private String username;

    public UsernameTakenException(String user) {
        super("Username '" + user + "' is already taken.");
        this.username = user;
    }

    public String getUsername() {
        return username;
    }
}
