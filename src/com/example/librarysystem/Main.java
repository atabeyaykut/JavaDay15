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

        // Demo Verileri Ekle
        Librarian librarian1 = new Librarian("L1", "Ayşe", "admin", "admin123", "EMP1001");
        libraryService.addLibrarian(librarian1);

        Reader reader1 = new Reader("R1", "Ali", "ali", "ali123");
        libraryService.addReader(reader1);

        Author author1 = new Author("A1", "J.K. Rowling");
        Book book1 = new Book("B1", "Harry Potter and the Philosopher's Stone", author1, "Fantastik");
        libraryService.addBook(book1);

        // Kullanıcı Girişi: Kullanıcı adı ve şifre ile giriş yapılır.
        AbstractPerson currentUser = null;
        boolean loggedIn = false;
        int loginAttempts = 0;
        while (!loggedIn && loginAttempts < 3) {
            System.out.print("Kullanıcı Adı: ");
            String username = scanner.nextLine();
            System.out.print("Şifre: ");
            String password = scanner.nextLine();
            try {
                currentUser = libraryService.login(username, password);
                loggedIn = true;
                System.out.println("Giriş başarılı! Hoşgeldiniz, " + currentUser.getName());
            } catch (LibraryException e) {
                System.out.println("Giriş başarısız: " + e.getMessage());
                loginAttempts++;
            }
        }

        if (!loggedIn) {
            System.out.println("Çok fazla başarısız giriş. Program sonlandırılıyor.");
            scanner.close();
            return;
        }

        // Giriş yapan kullanıcının tipine göre farklı menü sunulur.
        if (currentUser instanceof Librarian) {
            librarianMenu(scanner, libraryService);
        } else if (currentUser instanceof Reader) {
            readerMenu(scanner, libraryService, (Reader) currentUser);
        }

        scanner.close();
        System.out.println("Kütüphane sistemi sonlandırıldı.");
    }

    private static void librarianMenu(Scanner scanner, LibraryService libraryService) {
        boolean running = true;
        while (running) {
            displayCommonMenu();
            System.out.println("7) Kullanıcı Bilgilerini Görüntüle (Yönetici)");
            System.out.print("Seçiminiz: ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        // Kitap Ekleme
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
                        // Kitap Arama
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
                        // Kategorideki Kitapları Listele
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
                    case 7:
                        // Yöneticiye özel: Sistemdeki tüm okuyucuları listele.
                        System.out.println("=== Sistemdeki Tüm Okuyucular ===");
                        for (Reader r : libraryService.getAllReaders()) {
                            System.out.println("ID: " + r.getId() + ", İsim: " + r.getName());
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
            System.out.println(); // Görsel ayrım için boş satır
        }
    }

    private static void readerMenu(Scanner scanner, LibraryService libraryService, Reader currentReader) {
        boolean running = true;
        while (running) {
            displayCommonMenu();
            System.out.println("7) Okuyucu Bilgilerini Görüntüle");
            System.out.print("Seçiminiz: ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("Okuyucular kitap ekleyemez.");
                        break;
                    case 2:
                        // Kitap Arama
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
                        System.out.println("Okuyucular kitap silemez.");
                        break;
                    case 4:
                        // Kitap Ödünç Alma
                        System.out.print("Ödünç alınacak Kitap ID: ");
                        String borrowBookId = scanner.nextLine();
                        try {
                            libraryService.borrowBook(borrowBookId, currentReader.getId());
                        } catch (LibraryException le) {
                            System.out.println("Hata: " + le.getMessage());
                        }
                        break;
                    case 5:
                        // Kitap İade Etme
                        System.out.print("İade edilecek Kitap ID: ");
                        String returnBookId = scanner.nextLine();
                        try {
                            libraryService.returnBook(returnBookId, currentReader.getId());
                        } catch (LibraryException le) {
                            System.out.println("Hata: " + le.getMessage());
                        }
                        break;
                    case 6:
                        // Kategorideki Kitapları Listele
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
                    case 7:
                        // Okuyucuya özel: Kendi bilgilerini görüntüle.
                        System.out.println("=== Okuyucu Bilgileri ===");
                        System.out.println("ID: " + currentReader.getId() + ", İsim: " + currentReader.getName());
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
    }

    private static void displayCommonMenu() {
        System.out.println("=== KÜTÜPHANE OTOMASYONU ===");
        System.out.println("1) (Yönetici için: Kitap Ekle / Okuyucular için: Uygulanamaz)");
        System.out.println("2) Kitap Ara (ID)");
        System.out.println("3) (Yönetici için: Kitap Sil / Okuyucular için: Uygulanamaz)");
        System.out.println("4) Kitap Ödünç Al (KitapID, KullanıcıID)");
        System.out.println("5) Kitap İade Et (KitapID, KullanıcıID)");
        System.out.println("6) Kategorideki Kitapları Listele");
        System.out.println("0) Çıkış");
    }
}
