package com.example.librarysystem.model;

import java.util.HashSet;
import java.util.Set;

public class Reader extends AbstractPerson {
    private static final int MAX_BORROW_LIMIT = 5;
    private Set<Book> borrowedBooks;

    public Reader(String id, String name, String username, String password) {
        super(id, name, username, password);
        borrowedBooks = new HashSet<>();
    }

    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() >= MAX_BORROW_LIMIT) {
            System.out.println("Maksimum kitap limitine ulaştınız (5).");
            return false;
        }
        if (!book.isBorrowed()) {
            borrowedBooks.add(book);
            book.setBorrowed(true);
            return true;
        }
        System.out.println("Kitap şu anda başka bir kullanıcıda!");
        return false;
    }

    public boolean returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            book.setBorrowed(false);
            return true;
        }
        System.out.println("Bu kitabı ödünç almadınız.");
        return false;
    }

    public Set<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
