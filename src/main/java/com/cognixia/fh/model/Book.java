package com.cognixia.fh.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private int totalPages;
    
    public Book(int id, String title, String author, int totalPages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
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

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", totalPages=" + totalPages + "]";
    }
}
