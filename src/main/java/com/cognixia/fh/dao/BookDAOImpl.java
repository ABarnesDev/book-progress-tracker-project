package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.model.Book;
import com.cognixia.fh.model.User;

public class BookDAOImpl implements BookDAO {

    // Retrieve all books from the database
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        // SQL query to select all books
        String sql = "SELECT book_id, title, author, total_pages FROM book";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            // Create Book objects for each row in the result set and add them to the list
            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalPages = rs.getInt("total_pages");

                Book book = new Book(id, title, author, totalPages);
                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books");
        }

        return books;
    }

    // Retrieve a book by its ID from the database
    @Override
    public Book getBookById(int id) {
        Book book = null;

        // SQL query to select a book by id
        String sql = "SELECT book_id, title, author, total_pages FROM book WHERE book_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameter for the prepared statement
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                // If a book is found, create a Book object
                if (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int totalPages = rs.getInt("total_pages");

                    book = new Book(id, title, author, totalPages);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving book");
        }

        return book;
    }

    // Retrieve all the books that a specific user is not tracking 
    @Override
    public List<Book> getBooksNotTrackedByUser(int userid) {
        List<Book> books = new ArrayList<>();

        // SQL query to select books not being tracked by the user
        String sql = "SELECT book_id, title, author, total_pages FROM book WHERE book_id NOT IN " +
                     "(SELECT book_id FROM tracker WHERE user_id = ?)";

        try (Connection conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameter for the prepared statement
            ps.setInt(1, userid);

            try (ResultSet rs = ps.executeQuery()) {
                // Create Book objects for each row in the result set and add them to the list
                while (rs.next()) {
                    int id = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int totalPages = rs.getInt("total_pages");

                    Book book = new Book(id, title, author, totalPages);
                    books.add(book);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books");
        }

        return books;
    }

    // Add a new book to the database
    @Override
    public boolean addBook(User user, String title, String author, int totalPages) {
        // If the user isn't an admin, don't add the book to the database
        if (user.isAdmin() == false) {
            return false;
        }
        boolean isCreated = false;

        // SQL query to add a book to the database
        String sql = "INSERT INTO book (title, author, total_pages) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, totalPages);

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the book was created successfully
            if (rowsAffected > 0) {
                isCreated = true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error adding book");
        }
        return isCreated;
    }

    // Update an existing book in the database
    @Override
    public boolean updateBook(User user, int id, String title, String author, int totalPages) {
        // If the user isn't an admin, don't update the book
        if (user.isAdmin() == false) {
            return false;
        }
        boolean isUpdated = false;

        // SQL query to update an existing book
        String sql = "UPDATE book SET title = ?, author = ?, total_pages = ? WHERE book_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, totalPages);
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();
            
            // If rowsAffected is greater than 0, the book was updated successfully
            if (rowsAffected > 0) {
                isUpdated = true;
            }

        } catch (SQLException e) {
            System.out.println("Error updating book");
        }
        return isUpdated;
    }

    // Delete a book from the database by its ID
    @Override
    public boolean deleteBook(User user, int id) {
        // If the user isn't an admin, don't delete the book
        if (user.isAdmin() == false) {
            return false;
        }
        boolean isDeleted = false;

        // SQL query to delete a book
        String sql = "DELETE FROM book WHERE book_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the book was deleted successfully
            if (rowsAffected > 0) {
                isDeleted = true;
            }

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Cannot delete a book that is currently being tracked by a user");
         }catch (SQLException e) {
            System.out.println("Error deleting book");
            e.printStackTrace();
        }
        return isDeleted;
    }

    

}
