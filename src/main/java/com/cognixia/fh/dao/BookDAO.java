package com.cognixia.fh.dao;

import java.util.List;

import com.cognixia.fh.model.Book;
import com.cognixia.fh.model.User;

public interface BookDAO {
    public List<Book> getAllBooks();
    public Book getBookById(int id);
    public List<Book> getBooksNotTrackedByUser(int userid);
    public boolean addBook(User user, String title, String author, int totalPages);
    public boolean updateBook(User user, int id, String title, String author, int totalPages);
    public boolean deleteBook(User user, int id);
}
