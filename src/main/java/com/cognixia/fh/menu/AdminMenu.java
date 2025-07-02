package com.cognixia.fh.menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.cognixia.fh.dao.BookDAO;
import com.cognixia.fh.dao.BookDAOImpl;
import com.cognixia.fh.model.Book;
import com.cognixia.fh.model.User;

public class AdminMenu {
    private final Scanner scanner;
    private final BookDAO bookDAO = new BookDAOImpl();
    private User currentUser;

    public AdminMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void showMenu() {
        boolean exit = false;
        System.out.println("\nWelcome to the admin menu.");
        System.out.println("Here, you can add, update, and delete books from our database.");

        while (!exit) {
            System.out.println("What would you like to do?\n");

            System.out.println("1. View all books in the database");
            System.out.println("2. Add a book");
            System.out.println("3. Update a book");
            System.out.println("4. Delete a book");
            System.out.println("5. Exit admin menu");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewAllBooks();
                    break;
                case "2":
                    addBook();
                    break;
                case "3":
                    updateBook();
                    break;
                case "4":
                    deleteBook();
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    System.out.println("\nPlease enter a valid option (1-5).");
            }
        }
    }

    // Displays all books in the database
    private List<Book> viewAllBooks() {
        List<Book> books = bookDAO.getAllBooks();
        System.out.println("\nHere is a list of all the books in the database.\n");
        books.forEach(System.out::println);

        return books;
    }

    // Add a book to the database
    private void addBook() {
        String title = getStringFromUser("\nEnter book title:");
        String author = getStringFromUser("\nEnter book author:");
        int totalPages = getPagesFromUser();

        boolean added = bookDAO.addBook(currentUser, title, author, totalPages);
        if (added) {
            System.out.println("\nBook added successfully.");
        } else {
            System.out.println("\nFailed to add book.");
        }
    }

    // Update a book in the database
    private void updateBook() {
        List<Book> books = viewAllBooks();
        System.out.println("\nEnter the ID of the book to update:");

        // Get the book to update from the user
        Book book = getBookFromUser(books, "update");
        // If the user doesn't select a book, exit the method
        if (book == null) return;

        // Get the title, author, and total pages from the user
        String title = getStringFromUser("\nEnter new title:");
        String author = getStringFromUser("\nEnter new author:");
        int totalPages = getPagesFromUser();

        // Update the book in the database
        boolean updated = bookDAO.updateBook(currentUser, book.getId(), title, author, totalPages);
        if (updated) {
            System.out.println("\nBook updated successfully.");
        } else {
            System.out.println("\nFailed to update book.");
        }
    }

    // Delete a book from the database
    private void deleteBook() {
        List<Book> books = viewAllBooks();
        System.out.println("\nEnter the ID of the book to delete:");

        // Get the book to delete from the user
        Book book = getBookFromUser(books, "delete");
        // If the user doesn't select a book, exit the method
        if (book == null) return;

        // Delete the book from the database
        boolean deleted = bookDAO.deleteBook(currentUser, book.getId());
        if (deleted) {
            System.out.println("\nBook deleted successfully.");
        } else {
            System.out.println("\nFailed to delete book.");
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

    // Prompts the user for the total pages of a book and ensures a positive integer is returned
    private int getPagesFromUser() {
        int pages = 0;

        System.out.println("\nEnter total pages:");
        while (pages <= 0) {
            try {
                pages = Integer.parseInt(scanner.nextLine());

                if (pages <= 0) {
                    System.out.println("\nEnter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a valid number:");
            }
        }

        return pages;
    }

    // Prompts the user to select a book
    private Book getBookFromUser(List<Book> books, String action) {
        Book book = null;

        // Loop until the user confirms the book or cancels
        boolean bookConfirmed = false;
        while (!bookConfirmed) {
            // Ask the user to select a book if they haven't already
            if (book == null) {
                System.out.println("\nEnter the id of the book you want to " + action + ":");
                System.out.println("Enter 'c' to cancel this operation");

                try {
                    String input = scanner.nextLine();

                    // If the user enters 'c' return null
                    if (input.equalsIgnoreCase("c")) return null;

                    int bookId = Integer.parseInt(input);

                    // Use bookId to find the selected book from the list of books
                    book = books.stream().filter(b -> b.getId() == bookId).findFirst().get();
                } catch (NoSuchElementException e) {
                    System.out.println("\nThe id you entered does not exist.");
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Please enter a number.");
                }
            } else {
                // Confirm with the user before selecting the book
                System.out.println("\nHere is the book you want to " + action + ":\n");
                System.out.println(book);
                System.out.println("\nAre you sure you want to " + action + " this book? (y/n)");

                String input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y":
                        bookConfirmed = true;
                        break;
                    case "n":
                        book = null;
                        break;
                    default:
                        System.out.println("\nPlease enter 'y' or 'n'");
                        break;
                }
            }
        }
        return book;
    }
}
