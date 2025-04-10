package com.example.librarysystem.model;

public class Book {
    private String id;
    private String title;
    private Author author;
    private String category;
    private boolean isBorrowed;

    public Book(String id, String title, Author author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isBorrowed = false;
        author.addBook(this);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.isBorrowed = borrowed;
    }
}
