package com.cognixia.fh.dao;

import java.util.List;

import com.cognixia.fh.exception.InvalidPagesException;
import com.cognixia.fh.exception.InvalidRatingException;
import com.cognixia.fh.model.Status;
import com.cognixia.fh.model.TrackedBook;

public interface TrackerDAO {
    public boolean addTracker(int userId, int bookId, Status status, int pagesRead, int totalPages, int rating) throws InvalidRatingException, InvalidPagesException;
    public boolean updateTracker(int userId, int bookId, Status status, int pagesRead, int totalPages, int rating) throws InvalidRatingException, InvalidPagesException;
    public boolean deleteTracker(int userId, int bookId);
    public List<TrackedBook> getTrackedBooksByUserId(int userId);
}
