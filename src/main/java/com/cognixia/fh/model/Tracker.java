package com.cognixia.fh.model;

public class Tracker {
    public enum Status {
        PLAN_TO_READ, CURRENTLY_READING, FINISHED_READING
    }

    private int userId;
    private int bookId;
    private Status status;
    private int pagesRead;
    private int rating; // Must be between 1 and 5
    
    public Tracker(int userId, int bookId, Status status, int pagesRead) {
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
        this.pagesRead = pagesRead;
    } 

    public Tracker(int userId, int bookId, Status status, int pagesRead, int rating) {
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
        this.pagesRead = pagesRead;
        setRating(rating); // Use setRating to ensure rating is valid
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        // Rating must be between 1 and 5
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Tracker [userId=" + userId + ", bookId=" + bookId + ", status=" + status + ", pagesRead=" + pagesRead
                + ", rating=" + rating + "]";
    }
}
