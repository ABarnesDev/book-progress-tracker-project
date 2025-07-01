package com.cognixia.fh;

import java.util.Scanner;

import com.cognixia.fh.menu.LoginMenu;
import com.cognixia.fh.menu.UserMenu;

// Main class to start the Progress Tracker application
public class Main {
    // Shared Scanner instance for user input
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Main application loop
        while (true) {
            // Show login menu and handle user authentication
            LoginMenu loginMenu = new LoginMenu(scanner);
            loginMenu.showMenu();

            // If no user is logged in, exit the application
            if (loginMenu.getCurrentUser() == null) {
                System.out.println("\nExiting the application. Goodbye!");
                break;
            }

            // Show user menu for authenticated user
            UserMenu userMenu = new UserMenu(scanner, loginMenu.getCurrentUser());
            userMenu.showMenu();
        }
    }
}