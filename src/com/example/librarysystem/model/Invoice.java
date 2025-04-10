package com.example.librarysystem.model;

public abstract class Invoice {
    protected String invoiceId;
    protected Reader reader;
    protected Book book;
    protected double amount;

    public Invoice(String invoiceId, Reader reader, Book book, double amount) {
        this.invoiceId = invoiceId;
        this.reader = reader;
        this.book = book;
        this.amount = amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public double getAmount() {
        return amount;
    }

    public abstract void pay();
    public abstract void refund();
}
