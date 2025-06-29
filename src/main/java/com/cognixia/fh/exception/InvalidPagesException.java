package com.cognixia.fh.exception;

public class InvalidPagesException extends Exception {
    private int totalPages;

    public InvalidPagesException(int totalPages) {
        super("The number of pages read must be between 0 and " + totalPages);
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
