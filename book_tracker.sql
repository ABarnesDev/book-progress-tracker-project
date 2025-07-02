DROP DATABASE IF EXISTS book_tracker;
CREATE DATABASE book_tracker;
USE book_tracker;

CREATE TABLE user (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    total_pages INT NOT NULL
);

CREATE TABLE tracker (
    user_id INT,
    book_id INT,
    status ENUM('PLAN_TO_READ', 'CURRENTLY_READING', 'FINISHED_READING') NOT NULL,
    pages_read INT DEFAULT 0,
    rating SMALLINT CHECK (rating BETWEEN 1 AND 5),
    PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

INSERT INTO user (username, password, is_admin) VALUES ('admin', 'admin@123', 1);
INSERT INTO book (title, author, total_pages) VALUES
('Moby-Dick', 'Herman Melville', 635),
('1984', 'George Orwell', 328),
('To Kill a Mockingbird', 'Harper Lee', 281),
('The Great Gatsby', 'F. Scott Fitzgerald', 180),
('The Hobbit', 'J.R.R. Tolkien', 310),
('Harry Potter and the Philosopher\'s Stone', 'J.K. Rowling', 223),
('The Lion, the Witch and the Wardrobe', 'C. S. Lewis', 172),
('Atomic Habits', 'James Clear', 320),
('The Warmth of Other Suns', 'Isabel Wilkerson', 622),
('Dune', 'Frank Herbert', 896);