package com.cognixia.fh.dao;

import java.util.List;

import com.cognixia.fh.model.Tracker;
import com.cognixia.fh.model.Tracker.Status;

public interface TrackerDAO {
    public boolean addTracker(int userId, int bookId, Status status, int pagesRead, int rating);
    public boolean updateTracker(int userId, int bookId, Status status, int pagesRead, int rating);
    public boolean deleteTracker(int userId, int bookId);
    public List<Tracker> getTrackersByUserId(int userId);
}
