package com.cognixia.fh.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

// This class will manage the connections to the database.
public class ConnectionManager {
    private static Connection connection;

    private ConnectionManager(){}

    private static void makeConnection() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/config.properties"));

        // Load the database connection properties from the config file
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        // Load the MySQL JDBC driver and establish the connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = java.sql.DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        // If a connection does not exist, create a new one. Otherwise, return the existing connection.
        if (connection == null) {
            makeConnection();
        }
        return connection;
    }
}
