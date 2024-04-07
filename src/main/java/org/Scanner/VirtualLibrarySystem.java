package org.Scanner;

import java.util.List;
import java.util.Scanner;

public class VirtualLibrarySystem {
    public static void main(String[] args) {
        Library library = new Library();
        library.batchUploadBooks("C:/Users/Admin/Downloads/books.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search criteria: ");
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