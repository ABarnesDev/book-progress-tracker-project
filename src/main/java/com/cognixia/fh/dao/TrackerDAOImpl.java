package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.exception.InvalidPagesException;
import com.cognixia.fh.exception.InvalidRatingException;
import com.cognixia.fh.model.Status;
import com.cognixia.fh.model.TrackedBook;

public class TrackerDAOImpl implements TrackerDAO {

    // Add a new tracker to the database
    @Override
    public boolean addTracker(int userId, int bookId, Status status, int pagesRead, int totalPages, int rating) throws InvalidRatingException, InvalidPagesException {
        // Throw an error if the number of pages read is negative or more than the total number of pages
        if (pagesRead < 0 || pagesRead > totalPages) {
            throw new InvalidPagesException(totalPages);
        }

        boolean isCreated = false;

        // SQL query to insert a new tracker
        String sql = "INSERT INTO tracker (user_id, book_id, status, pages_read, rating) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setString(3, status.name());
            ps.setInt(4, pagesRead);
            // If rating is 0, set it to NULL in the database
            if (rating == 0) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, rating);
            }

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the tracker was created successfully
            if (rowsAffected > 0) {
                isCreated = true;
            }

        } catch (SQLException e) {
            // If the rating is out of range, the error code will be 3819.
            if (e.getErrorCode() == 3819) {
                throw new InvalidRatingException();
            } else {
                System.out.println("Error tracking book");
            }
        }

        return isCreated;
    }

    // Update an existing tracker in the database
    @Override
    public boolean updateTracker(int userId, int bookId, Status status, int pagesRead, int totalPages, int rating) throws InvalidRatingException, InvalidPagesException {
        // Throw an error if the number of pages read is negative or more than the total number of pages
        if (pagesRead < 0 || pagesRead > totalPages) {
            throw new InvalidPagesException(totalPages);
        }

        boolean isUpdated = false;

        // SQL query to update an existing tracker
        String sql = "UPDATE tracker SET status = ?, pages_read = ?, rating = ? WHERE user_id = ? AND book_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setString(1, status.name());
            ps.setInt(2, pagesRead);
            // If rating is 0, set it to NULL in the database
            if (rating == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, rating);
            }
            ps.setInt(4, userId);
            ps.setInt(5, bookId);

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the tracker was updated successfully
            if (rowsAffected > 0) {
                isUpdated = true;
            }

        } catch (SQLException e) {
            // If the rating is out of range, the error code will be 3819.
            if (e.getErrorCode() == 3819) {
                throw new InvalidRatingException();
            } else {
                System.out.println("Error updating tracker");
            }
        }

        return isUpdated;
    }

    // Delete a tracker from the database
    @Override
    public boolean deleteTracker(int userId, int bookId) {
        boolean isDeleted = false;

        // SQL query to delete a tracker
        String sql = "DELETE FROM tracker WHERE user_id = ? AND book_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setInt(1, userId);
            ps.setInt(2, bookId);

            int rowsAffected = ps.executeUpdate();

            // If rowsAffected is greater than 0, the tracker was deleted successfully
            if (rowsAffected > 0) {
                isDeleted = true;
            }

        } catch (SQLException e) {
            System.out.println("Error deleting tracker");
        }

        return isDeleted;
    }

    // Retrieve the books and tracking details for books that are currently being tracked by the user
    @Override
    public List<TrackedBook> getTrackedBooksByUserId(int userId) {
        List<TrackedBook> trackedBooks = new ArrayList<>();

        // SQL query to select tracked books and their details
        String sql = "SELECT t.book_id, b.title, b.author, t.status, t.pages_read, b.total_pages, t.rating " +
                     "FROM tracker t " +
                     "JOIN book b ON t.book_id = b.book_id " +
                     "WHERE t.user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameter for the prepared statement
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                // Iterate through the result set and create TrackedBook objects
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    Status status = Status.valueOf(rs.getString("status"));
                    int pagesRead = rs.getInt("pages_read");
                    int totalPages = rs.getInt("total_pages");
                    int rating = rs.getInt("rating");

                    // If rating is Null, create TrackedBook without a rating
                    TrackedBook trackedBook;
                    if (rs.wasNull()) {
                        trackedBook = new TrackedBook(bookId, title, author, status, pagesRead, totalPages);
                    } else {
                        trackedBook = new TrackedBook(bookId, title, author, status, pagesRead, totalPages, rating);
                    }

                    trackedBooks.add(trackedBook);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error retrieving tracked books.");
        }

        return trackedBooks;
    }
}
