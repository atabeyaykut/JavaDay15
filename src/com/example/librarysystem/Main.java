package com.example.librarysystem;

import com.example.librarysystem.model.*;
import com.example.librarysystem.exception.LibraryException;
import com.example.librarysystem.service.LibraryService;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = LibraryService.getInstance();
        Scanner scanner = new Scanner(System.in);

        // Demo Veri
        Author author1 = new Author("A1", "J.K. Rowling");
        Book book1 = new Book("B1", "Harry Potter and the Philosopher's Stone", author1, "Fantastik");
        libraryService.addBook(book1);

        Reader reader1 = new Reader("R1", "Ali");
        libraryService.addReader(reader1);

        Librarian librarian1 = new Librarian("L1", "Ayşe", "EMP1001");
        libraryService.addLibrarian(librarian1);

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Seçiminiz: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        // Yeni Kitap Ekleme
                        System.out.print("Kitap ID: ");
                        String bookId = scanner.nextLine();
                        System.out.print("Kitap Adı: ");
                        String title = scanner.nextLine();
                        System.out.print("Yazar Adı: ");
                        String authorName = scanner.nextLine();
                        System.out.print("Kategori: ");
                        String category = scanner.nextLine();
                        Author newAuthor = new Author("A_" + authorName, authorName);
                        Book newBook = new Book(bookId, title, newAuthor, category);
                        libraryService.addBook(newBook);
                        break;
                    case 2:
                        // Kitap Arama (ID)
                        System.out.print("Aranacak Kitap ID: ");
                        String searchId = scanner.nextLine();
                        Book foundBook = libraryService.searchBookById(searchId);
                        if (foundBook != null) {
                            System.out.println("Kitap Bulundu: " + foundBook.getTitle() + " - " + foundBook.getCategory());
                        } else {
                            System.out.println("Kitap bulunamadı!");
                        }
                        break;
                    case 3:
                        // Kitap Silme
                        System.out.print("Silinecek Kitap ID: ");
                        String removeId = scanner.nextLine();
                        try {
                            libraryService.removeBookById(removeId);
                        } catch (LibraryException le) {
                            System.out.println("Hata: " + le.getMessage());
                        }
                        break;
                    case 4:
                        // Kitap Ödünç Alma
                        System.out.print("Ödünç alınacak Kitap ID: ");
                        String borrowBookId = scanner.nextLine();
                        System.out.print("Okuyucu ID: ");
                        String borrowReaderId = scanner.nextLine();
                        try {
                            libraryService.borrowBook(borrowBookId, borrowReaderId);
                        } catch (LibraryException le) {
                            System.out.println("Hata: " + le.getMessage());
                        }
                        break;
                    case 5:
                        // Kitap İade Etme
                        System.out.print("İade edilecek Kitap ID: ");
                        String returnBookId = scanner.nextLine();
                        System.out.print("Okuyucu ID: ");
                        String returnReaderId = scanner.nextLine();
                        try {
                            libraryService.returnBook(returnBookId, returnReaderId);
                        } catch (LibraryException le) {
                            System.out.println("Hata: " + le.getMessage());
                        }
                        break;
                    case 6:
                        // Kategoriye Göre Kitap Listeleme
                        System.out.print("Kategori: ");
                        String listCategory = scanner.nextLine();
                        List<Book> booksByCategory = libraryService.listBooksByCategory(listCategory);
                        if (booksByCategory.isEmpty()) {
                            System.out.println("Bu kategoriye ait kitap bulunamadı.");
                        } else {
                            System.out.println("Kategori: " + listCategory);
                            booksByCategory.forEach(b -> System.out.println(b.getId() + " - " + b.getTitle()));
                        }
                        break;
                    case 0:
                        running = false;
                        System.out.println("Sistem kapatılıyor...");
                        break;
                    default:
                        System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lütfen geçerli bir sayı giriniz.");
            }
            System.out.println();
        }
        scanner.close();
        System.out.println("Kütüphane sistemi sonlandırıldı.");
    }

    private static void displayMenu() {
        System.out.println("=== KÜTÜPHANE OTOMASYONU ===");
        System.out.println("1) Kitap Ekle");
        System.out.println("2) Kitap Ara (ID)");
        System.out.println("3) Kitap Sil (ID)");
        System.out.println("4) Kitap Ödünç Al (KitapID, OkuyucuID)");
        System.out.println("5) Kitap İade Et (KitapID, OkuyucuID)");
        System.out.println("6) Kategorideki Kitapları Listele");
        System.out.println("0) Çıkış");
    }
}
