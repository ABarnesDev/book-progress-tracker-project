package com.cognixia.fh.dao;

import java.util.List;

import com.cognixia.fh.model.Book;

public interface BookDAO {
    public List<Book> getAllBooks();
    public Book getBookById(int id);
    public List<Book> getBooksNotTrackedByUser(int userid);
}
