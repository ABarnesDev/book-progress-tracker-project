package com.cognixia.fh.menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.cognixia.fh.dao.BookDAO;
import com.cognixia.fh.dao.BookDAOImpl;
import com.cognixia.fh.dao.TrackerDAO;
import com.cognixia.fh.dao.TrackerDAOImpl;
import com.cognixia.fh.exception.InvalidPagesException;
import com.cognixia.fh.exception.InvalidRatingException;
import com.cognixia.fh.model.Book;
import com.cognixia.fh.model.Status;
import com.cognixia.fh.model.TrackedBook;
import com.cognixia.fh.model.User;

// UserMenu class handles the user interface for book tracking operations
public class UserMenu {
    private final Scanner scanner;
    private final BookDAO bookDAO = new BookDAOImpl();
    private final TrackerDAO trackerDAO = new TrackerDAOImpl();
    private User currentUser;

    public UserMenu(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    // Displays the main menu and handles user input
    public void showMenu() {
        System.out.println("\nWelcome to the user menu.");
        System.out.println("Here, you can choose from the books in our database to add to your reading list.");
        System.out.println("You can keep track of your reading progress for any books you add to your reading list.");

        // Loop until the user logs out
        while (currentUser != null) {
            System.out.println("What would you like to do?\n");

            System.out.println("1. View all books in the database");
            System.out.println("2. View reading list");
            System.out.println("3. Add a book to my reading list");
            System.out.println("4. Update one of my books");
            System.out.println("5. Remove a book from my reading list");
            System.out.println("6. Logout");
            if (currentUser.isAdmin()) System.out.println("7. Admin menu");
            
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewAllBooks();
                    break;
                case "2":
                    viewTrackedBooksByStatus();
                    break;
                case "3":
                    trackBook();
                    break;
                case "4":
                    updateTrackedBook();
                    break;
                case "5":
                    removeTrackedBook();
                    break;
                case "6":
                    System.out.println("\nLogging out.");
                    currentUser = null;
                    break;
                case "7":
                    if (currentUser.isAdmin()) {
                        AdminMenu adminMenu = new AdminMenu(scanner, currentUser);
                        adminMenu.showMenu();
                        break;
                    }
                default:
                    System.out.println("\nPlease enter an option listed (number 1 - 6)");
                    break;
            }

        }

    }

    // Displays all books in the database
    private void viewAllBooks() {
        List<Book> books = bookDAO.getAllBooks();
        System.out.println("\nHere is a list of all the books in the database.\n");
        books.forEach(System.out::println);
    }

    private void viewTrackedBooksByStatus() {
        List<TrackedBook> trackedBooks = null;
        Status status = null;

        while (trackedBooks == null) {
            System.out.println("\nDo you want to view all the books in your reading list, the books you plan to read, the books you are currently reading, or the books you have finished reading?\n");

            System.out.println("1. All books in reading list");
            System.out.println("2. Books I plan to read");
            System.out.println("3. Books I am currently reading");
            System.out.println("4. Books I have finished reading");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    trackedBooks = trackerDAO.getTrackedBooksByUserId(currentUser.getId());
                    break;
                case "2":
                    status = Status.PLAN_TO_READ;
                    trackedBooks = trackerDAO.getTrackedBooksByUserIdAndStatus(currentUser.getId(), status);
                    break;
                case "3":
                    status = Status.CURRENTLY_READING;
                    trackedBooks = trackerDAO.getTrackedBooksByUserIdAndStatus(currentUser.getId(), status);
                    break;
                case "4":
                    status = Status.FINISHED_READING;
                    trackedBooks = trackerDAO.getTrackedBooksByUserIdAndStatus(currentUser.getId(), status);
                    break;
                default:
                    System.out.println("\nPlease enter an option listed (number 1 - 4)");
            }
        }

        // If the user is not tracking any books, inform them
        if (trackedBooks.isEmpty()) {
            if (status == null) {
                System.out.println("\nThere are no books in your reading list.");
            } else {
                System.out.println("\nYou are not tracking any books with the status: " + status);
            }
        } else {
            // Display the tracked books
            System.out.println("\nHere is your reading list:\n");
            trackedBooks.forEach(System.out::println);
        }
    }

    // Displays all books tracked by the current user
    private List<TrackedBook> viewAllTrackedBooks() {
        List<TrackedBook> trackedBooks = trackerDAO.getTrackedBooksByUserId(currentUser.getId());

        if (trackedBooks.isEmpty()) {
            System.out.println("\nThere are no books in your reading list.");
        } else {
            System.out.println("\nHere is your reading list:\n");
            trackedBooks.forEach(System.out::println);
        }
        return trackedBooks;
    }

    // Allows the user to start tracking a new book
    private void trackBook() {
        // Get the books that the user is not currently tracking
        List<Book> untrackedBooks = bookDAO.getBooksNotTrackedByUser(currentUser.getId());
        // If there are no books for the user to track, exit the method
        if (untrackedBooks.isEmpty()) {
            System.out.println("\nThere are no books for you to add");
            return;
        }

        // Display untracked books to the user
        System.out.println("\nHere is a list of all the books that are not in your reading list\n");
        untrackedBooks.forEach(System.out::println);

        // Get the book to track from the user
        Book book = getBookFromUser(untrackedBooks, "add");
        // If the user doesn't select a book, exit the method
        if (book == null) return;

        // Get status, pages read, and rating from user
        Status status = getStatusFromUser();
        int pagesRead = getPagesReadFromUser(status, book.getTotalPages());
        int rating = getRatingFromUser(status);

        // Try to add the book to the user's tracked books
        // Inform the user if they entered an invalid value for the book's rating or number of pages read
        try {
            boolean isTracked = trackerDAO.addTracker(currentUser.getId(), book.getId(), status, pagesRead, book.getTotalPages(), rating);

            if (isTracked) {
                System.out.println("\nThe book has been added to your reading list");
            }
        } catch (InvalidRatingException | InvalidPagesException e) {
            System.out.println(e.getMessage());
        }
    }

    // Allows the user to update a tracked book's status, pages read, or rating
    private void updateTrackedBook() {
        // Get the books that the user is currently tracking
        List<TrackedBook> trackedBooks = viewAllTrackedBooks();
        // If the user isn't tracking any books, exit the method
        if (trackedBooks.isEmpty()) return;

        // Get the book to update from the user
        TrackedBook book = getTrackedBookFromUser(trackedBooks, "update");
        // If the user doesn't select a book, exit the method
        if (book == null) return;

        // Get new status, pages read, and rating from user
        Status status = getStatusFromUser();
        int pagesRead = getPagesReadFromUser(status, book.getTotalPages());
        int rating = getRatingFromUser(status);

        // Try to update the tracked book
        // Inform the user if they entered an invalid value for the book's rating or number of pages read
        try {
            boolean isUpdated = trackerDAO.updateTracker(currentUser.getId(), book.getBookId(), status, pagesRead, book.getTotalPages(), rating);

            if (isUpdated) {
                System.out.println("\nYou have updated this book");
            }
        } catch (InvalidRatingException | InvalidPagesException e) {
            System.out.println(e.getMessage());
        }
    }

    // Allows the user to stop tracking a book
    private void removeTrackedBook() {
        // Get the books that the user is currently tracking
        List<TrackedBook> trackedBooks = viewAllTrackedBooks();
        // If the user isn't tracking any books, exit the method
        if (trackedBooks.isEmpty()) return;

        // Get the book to remove from the user
        TrackedBook book = getTrackedBookFromUser(trackedBooks, "remove");
        // If the user doesn't select a book, exit the method
        if (book == null) return;

        // Remove the tracker
        boolean isDeleted = trackerDAO.deleteTracker(currentUser.getId(), book.getBookId());

        if (isDeleted) {
            System.out.println("\nThis book has been removed from your reading list");
        }
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
                    System.out.println("\nThe id you entered does not exist or is already in your reading list.");
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

    // Prompts the user to select a book that is currently tracked
    private TrackedBook getTrackedBookFromUser(List<TrackedBook> trackedBooks, String action) {
        TrackedBook book = null;

        // Loop until the user confirms the book or cancels
        boolean bookConfirmed = false;
        while (!bookConfirmed) {
            // Ask the user to select a book if they haven't already
            if (book == null) {
                System.out.println("\nEnter the id of the book you want to " + action + ":");
                System.out.println("\nEnter 'c' to cancel this operation");

                try {
                    String input = scanner.nextLine();

                    // If the user enters 'c' return null
                    if (input.equalsIgnoreCase("c")) return null;

                    int bookId = Integer.parseInt(input);

                    // Use bookId to find the selected book from the list of tracked books
                    book = trackedBooks.stream().filter(b -> b.getBookId() == bookId).findFirst().get();
                } catch (NoSuchElementException e) {
                    System.out.println("\nThe id you entered does not exist or is not in your reading list.");
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

    // Prompts the user to select a reading status
    private Status getStatusFromUser() {
        Status status = null;

        while (status == null) {
            System.out.println("\nAre you planning to read, currently reading, or finished reading this book?");
            System.out.println("1. Planning to read");
            System.out.println("2. Currently reading");
            System.out.println("3. Finished reading");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    status = Status.PLAN_TO_READ;
                    break;
                case "2":
                    status = Status.CURRENTLY_READING;
                    break;
                case "3":
                    status = Status.FINISHED_READING;
                    break;
                default:
                    System.out.println("\nPlease enter an option listed (number 1 - 3)");
                    break;
            }
        }
        return status;
    }

    // Prompts the user to enter the number of pages read
    private int getPagesReadFromUser(Status status, int totalPages) {
        int pagesRead = 0;

        // If the user has finished reading, set pagesRead to totalPages automatically
        if (status == Status.FINISHED_READING) {
            pagesRead = totalPages;
        }

        // If the user is currently reading, prompt for pages read
        while (status == Status.CURRENTLY_READING) {
            System.out.println("\nHow many pages of this book have you read?");

            try {
                pagesRead = Integer.parseInt(scanner.nextLine());
                // Validate that pagesRead is within valid range
                if (pagesRead < 0 || pagesRead > totalPages) throw new InvalidPagesException(totalPages);

                break;
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
            } catch (InvalidPagesException e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        return pagesRead;
    }

    // Prompts the user to rate the book
    // Only allows rating if the status is not PLAN_TO_READ
    // User can skip rating by entering 0
    private int getRatingFromUser(Status status) {
        int rating = 0;

        // Only prompt for rating if the user is not planning to read
        while (status != Status.PLAN_TO_READ) {
            System.out.println("\nRate this book from 1-5. You can skip this step by entering 0.");

            try {
                rating = Integer.parseInt(scanner.nextLine());
                // Validate that rating is between 0 and 5
                if (rating < 0 || rating > 5) throw new InvalidRatingException();

                break;
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
            } catch (InvalidRatingException e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        return rating;
    }
}
