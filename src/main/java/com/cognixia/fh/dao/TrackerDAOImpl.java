package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.model.Tracker;
import com.cognixia.fh.model.Tracker.Status;

public class TrackerDAOImpl implements TrackerDAO {

    // Add a new tracker to the database
    @Override
    public boolean addTracker(int userId, int bookId, Status status, int pagesRead, int rating) {
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
            // If the rating is out of range, the error code will be 3819. If the book_id does not exist, the error code will be 1452.
            if (e.getErrorCode() == 3819) {
                System.out.println("Error: Rating must be between 1 and 5.");
            } else if (e.getErrorCode() == 1452) {
                System.out.println("Book ID does not exist.");
            } else {
                System.out.println("Error tracking book");
            }
        }

        return isCreated;
    }

    // Update an existing tracker in the database
    @Override
    public boolean updateTracker(int userId, int bookId, Status status, int pagesRead, int rating) {
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
                System.out.println("Error: Rating must be between 1 and 5.");
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

    // Retrieve all trackers for a specific user from the database
    @Override
    public List<Tracker> getTrackersByUserId(int userId) {
        List<Tracker> trackers = new ArrayList<>();

        // SQL query to select trackers by user ID
        String sql = "SELECT user_id, book_id, status, pages_read, rating FROM tracker WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the parameter for the prepared statement
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            // Iterate through the result set and create Tracker objects
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                Status status = Status.valueOf(rs.getString("status"));
                int pagesRead = rs.getInt("pages_read");
                Integer rating = rs.getInt("rating");

                // If rating is Null, create Tracker without rating
                Tracker tracker;
                if (rs.wasNull()) {
                    tracker = new Tracker(userId, bookId, status, pagesRead);
                } else {
                    tracker = new Tracker(userId, bookId, status, pagesRead, rating);
                }

                trackers.add(tracker);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving trackers");
        }

        return trackers;
    }
}
