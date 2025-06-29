package com.cognixia.fh.dao;

import com.cognixia.fh.exception.UserNotFoundException;
import com.cognixia.fh.exception.UsernameTakenException;
import com.cognixia.fh.model.User;

public interface UserDAO {

    public boolean createUser(String username, String password) throws UsernameTakenException;
    public User loginUser(String username, String password) throws UserNotFoundException;

}
