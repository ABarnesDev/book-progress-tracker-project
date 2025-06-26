package com.cognixia.fh.connection;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

// This class will manage the connections to the database.
public class ConnectionManager {
    private static Connection connection;

    private ConnectionManager(){}

    private static void makeConnection() {

        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/config.properties"));

            // Load the database connection properties from the config file
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            // Load the MySQL JDBC driver and establish the connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            
        } catch (Exception e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        // If a connection does not exist, create a new one. Otherwise, return the existing connection.
        if (connection == null) {
            makeConnection();
        }
        return connection;
    }
}
