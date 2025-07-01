package com.cognixia.fh.model;

public class TrackedBook {
    private int bookId;
    private String title;
    private String author;
    private Status status;
    private int pagesRead;
    private int totalPages;
    private int rating;
    
    public TrackedBook(int bookId, String title, String author, Status status, int pagesRead, int totalPages) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
        this.pagesRead = pagesRead;
        this.totalPages = totalPages;
    }

    public TrackedBook(int bookId, String title, String author, Status status, int pagesRead, int totalPages, int rating) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
        this.pagesRead = pagesRead;
        this.totalPages = totalPages;
        this.rating = rating;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        String ratingString = (rating > 0) ? (rating + "/5") : "N/A";
        return "id = " + bookId + ", Title = " + title + ", Author = " + author + ", Status = " + status
                + ", Pages Read = " + pagesRead + "/" + totalPages + ", Rating = " + ratingString + "\n";
    }
}
