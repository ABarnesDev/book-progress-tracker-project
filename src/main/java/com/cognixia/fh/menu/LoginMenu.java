package com.cognixia.fh.menu;

import java.util.Scanner;

import com.cognixia.fh.dao.UserDAO;
import com.cognixia.fh.dao.UserDAOImpl;
import com.cognixia.fh.exception.UserNotFoundException;
import com.cognixia.fh.exception.UsernameTakenException;
import com.cognixia.fh.model.User;

// LoginMenu class handles the functionality to create and login to user accounts
public class LoginMenu {
    private final Scanner scanner;
    private final UserDAO userDAO = new UserDAOImpl();
    // Currently logged in user
    private User currentUser;

    public LoginMenu(Scanner scanner) {
        this.scanner = scanner;
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Displays the login menu and handles user choices
    public void showMenu() {
        System.out.println("\nWelcome to the book progress tracker project.");
        System.out.println("This application will allow you to keep track of a reading list.");

        boolean closeMenu = false;

        // Loop until user logs in or chooses to exit
        while (!closeMenu && currentUser == null) {
            System.out.println("\nYou are currently viewing the main menu.");
            System.out.println("What would you like to do?\n");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    register();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    closeMenu = true; // Exit the menu
                    break;
                default:
                    System.out.println("\nPlease enter an option listed (number 1 - 3)");
                    break;
            }
        }
    }

    // Handles user registration
    private void register() {
        String username = getStringFromUser("\nEnter the username for your new account:");
        String password = getStringFromUser("\nEnter the password for your new account:");

        // Try to create a new user account using the username and password entered.
        // Inform the user if the username is already taken
        try {
            boolean isCreated = userDAO.createUser(username, password);

            if (isCreated) {
                System.out.println("\nYour account has been created. Please login.");
            }
        } catch (UsernameTakenException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    // Handles user login
    private void login() {
        String username = getStringFromUser("\nEnter your username:");
        String password = getStringFromUser("\nEnter your password:");

        // Try to login using the username and password entered by the user.
        // Inform the user if a user account with that username doesn't exist
        try {
            currentUser = userDAO.loginUser(username, password);
        } catch (UserNotFoundException e) {
            System.out.println("\n" + e.getMessage());
        }

        if (currentUser != null) {
            System.out.println("\nYou are logged in.");
        }
    }

    // Prompts the user for input and ensures a non-empty string is returned
    private String getStringFromUser(String message) {
        String string = "";

        while (string.isEmpty()) {
            System.out.println(message);
            string = scanner.nextLine();

            if (string.isEmpty()) {
                System.out.println("\nPlease enter a value.");
            }
        }
        return string;
    }
}
