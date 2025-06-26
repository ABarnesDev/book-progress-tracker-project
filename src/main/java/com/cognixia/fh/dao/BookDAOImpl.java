package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.model.Book;

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

            ResultSet rs = ps.executeQuery();

            // If a book is found, create a Book object
            if (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int totalPages = rs.getInt("total_pages");

                book = new Book(id, title, author, totalPages);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving book");
        }

        return book;
    }

}
