package com.example.librarysystem.model;

import java.util.HashSet;
import java.util.Set;

public class Author {
    private String authorId;
    private String authorName;
    private Set<Book> booksWritten;

    public Author(String authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.booksWritten = new HashSet<>();
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Set<Book> getBooksWritten() {
        return booksWritten;
    }

    public void addBook(Book book) {
        booksWritten.add(book);
    }
}
