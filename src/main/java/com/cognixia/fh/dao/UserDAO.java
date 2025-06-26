package com.cognixia.fh.dao;

import com.cognixia.fh.model.User;

public interface UserDAO {

    public boolean createUser(String username, String password);
    public User loginUser(String username, String password);

}
