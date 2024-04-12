package org.Scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class VirtualLibrarySystem {
    private Library library;
    public VirtualLibrarySystem() {
        this.library = new Library();
    }

    public static void main(String[] args) throws IOException {
        Library library = new Library();
        // String excelFilePath = System.getProperty("user.dir") + File.separator + "/Data/books.csv";
        String fileName = "books.csv";
        String[] parts = fileName.split("\\.");
        String extension = parts[1];
        String excelFilePath = Paths.get(System.getProperty("user.dir"), "Data", fileName).toString();
        //library.batchUploadBooks(excelFilePath);


        BatchUploaderFactory factory = new FileFormatBatchUploaderFactory();
        BookBatchUploaders uploader = factory.createUploader(library,extension);
        uploader.batchUploadBooks(library,excelFilePath);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter search criteria: ");
        String criteria = scanner.nextLine();
        List<Book> searchResults = library.searchBooks(criteria);

        if (searchResults.isEmpty()) {
            System.out.println("No books found matching the criteria.");
        } else {
            for (int i = 0; i < searchResults.size(); i++) {
                Book book = searchResults.get(i);
                System.out.println((i + 1) + ". " + book.getTitle() + " by " + book.getAuthor() + " (" + book.getISBN() + ")");
                System.out.println("-----------------------------------");
            }

            System.out.print("Enter the book number for more details: ");
            int selectedBookIndex = scanner.nextInt();
            Book selectedBook = searchResults.get(selectedBookIndex - 1);
            library.displayBookDetails(selectedBook);
        }

        scanner.close();
    }
}