package com.example.librarysystem.model;

public class StandardInvoice extends Invoice {

    public StandardInvoice(String invoiceId, Reader reader, Book book, double amount) {
        super(invoiceId, reader, book, amount);
    }

    @Override
    public void pay() {
        System.out.println("Invoice " + invoiceId + " için ödeme alındı: " + amount + " TL");
    }

    @Override
    public void refund() {
        System.out.println("Invoice " + invoiceId + " için iade gerçekleştirildi: " + amount + " TL");
    }
}
