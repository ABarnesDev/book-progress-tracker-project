package com.cognixia.fh.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private int totalPages;
    private double averageRating;
    
    public Book(int id, String title, String author, int totalPages, double averageRating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.averageRating = averageRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "id = " + id + ", Title = " + title + ", Author = " + author + ", Pages = " + totalPages + ", Average Rating = " + averageRating + "\n";
    }
}
