package com.example.librarysystem.service;

import com.example.librarysystem.model.*;
import com.example.librarysystem.exception.LibraryException;
import java.util.*;

public class LibraryService {
    private static LibraryService instance;
    private Map<String, Book> books;
    private Map<String, Reader> readers;
    private List<Librarian> librarians;
    private Map<String, Invoice> invoices;

    private LibraryService() {
        books = new HashMap<>();
        readers = new HashMap<>();
        librarians = new ArrayList<>();
        invoices = new HashMap<>();
    }

    public static synchronized LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    public void addBook(Book book) {
        if (books.containsKey(book.getId())) {
            System.out.println("Kitap zaten mevcut: " + book.getTitle());
        } else {
            books.put(book.getId(), book);
            System.out.println(book.getTitle() + " kütüphaneye eklendi.");
        }
    }

    public void removeBookById(String bookId) throws LibraryException {
        Book removed = books.remove(bookId);
        if (removed != null) {
            System.out.println(removed.getTitle() + " kütüphaneden silindi.");
        } else {
            throw new LibraryException("Silinecek kitap bulunamadı: " + bookId);
        }
    }

    public Book searchBookById(String id) {
        return books.get(id);
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String authorName) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().getAuthorName().equalsIgnoreCase(authorName)) {
                result.add(book);
            }
        }
        return result;
    }

    public void updateBookInfo(String bookId, String newTitle, String newCategory) throws LibraryException {
        Book book = books.get(bookId);
        if (book != null) {
            String oldTitle = book.getTitle();
            System.out.println("Kitap bilgileri güncellendi. Eski başlık: " + oldTitle + ", Yeni başlık: " + newTitle);
        } else {
            throw new LibraryException("Kitap bulunamadı: " + bookId);
        }
    }

    public void addReader(Reader reader) {
        if (readers.containsKey(reader.getId())) {
            System.out.println("Okuyucu zaten mevcut: " + reader.getName());
        } else {
            readers.put(reader.getId(), reader);
            System.out.println(reader.getName() + " sisteme eklendi.");
        }
    }

    public void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
        System.out.println("Kütüphaneci eklendi: " + librarian.getName());
    }

    public void borrowBook(String bookId, String readerId) throws LibraryException {
        Book book = books.get(bookId);
        Reader reader = readers.get(readerId);

        if (book == null) {
            throw new LibraryException("Kitap bulunamadı: " + bookId);
        }
        if (reader == null) {
            throw new LibraryException("Okuyucu bulunamadı: " + readerId);
        }
        if (book.isBorrowed()) {
            throw new LibraryException("Kitap şu anda ödünç alınmış: " + book.getTitle());
        }
        boolean success = reader.borrowBook(book);
        if (success) {
            String invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8);
            double amount = calculateRentalFee(book); // Örneğin sabit ücret belirlenmiştir.
            Invoice invoice = new StandardInvoice(invoiceId, reader, book, amount);
            invoices.put(invoiceId, invoice);
            invoice.pay();
            System.out.println(reader.getName() + " adlı kullanıcı " + book.getTitle() + " kitabını ödünç aldı.");
        }
    }

    public void returnBook(String bookId, String readerId) throws LibraryException {
        Book book = books.get(bookId);
        Reader reader = readers.get(readerId);
        if (book == null) {
            throw new LibraryException("Kitap bulunamadı: " + bookId);
        }
        if (reader == null) {
            throw new LibraryException("Okuyucu bulunamadı: " + readerId);
        }
        boolean success = reader.returnBook(book);
        if (success) {
            Invoice invoice = findInvoiceByBookAndReader(book, reader);
            if (invoice != null) {
                invoice.refund();
                invoices.remove(invoice.getInvoiceId());
            }
            System.out.println(reader.getName() + " adlı kullanıcı " + book.getTitle() + " kitabını iade etti.");
        } else {
            throw new LibraryException("Kitap iade edilemedi: " + book.getTitle());
        }
    }

    private double calculateRentalFee(Book book) {
        return 10.0;
    }

    private Invoice findInvoiceByBookAndReader(Book book, Reader reader) {
        for (Invoice inv : invoices.values()) {
            if (inv.getBook().equals(book) && inv.getReader().equals(reader)) {
                return inv;
            }
        }
        return null;
    }

    public List<Book> listBooksByCategory(String category) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                result.add(book);
            }
        }
        return result;
    }
}
