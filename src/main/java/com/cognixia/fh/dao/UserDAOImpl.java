package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.exception.UserNotFoundException;
import com.cognixia.fh.exception.UsernameTakenException;
import com.cognixia.fh.model.User;

public class UserDAOImpl implements UserDAO {

    // This method creates a new user
    @Override
    public boolean createUser(String username, String password) throws UsernameTakenException {
        // If the username or password is null or empty, return false
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        boolean isCreated = false;
        
        // SQL query to insert a new user
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setString(1, username);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the user was created successfully
            if (rowsAffected > 0) {
                isCreated = true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // This exception is thrown if the username already exists in the database
            throw new UsernameTakenException(username);

        } catch (SQLException e) {
            System.out.println("Error creating user");
        }
        
        return isCreated;
    }

    // This method gets a user from the database
    @Override
    public User loginUser(String username, String password) throws UserNotFoundException {
        User user = null;

        // SQL query to select a user by username and password
        String sql = "SELECT user_id, username, password, is_admin FROM user WHERE username = ? AND password = ?";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setString(1, username);
            ps.setString(2, password);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // If a user is found, create a User object. Otherwise, throw a UserNotFound exception
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                boolean isAdmin = rs.getBoolean("is_admin");

                user = new User(id, dbUsername, dbPassword, isAdmin);
            } else {
                throw new UserNotFoundException();
            }

        } catch (SQLException e) {
            System.out.println("Error logging in");
        }

        return user;
    }

}
