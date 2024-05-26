package org.Scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VirtualLibrarySystem {
    private static Library library;
    public VirtualLibrarySystem() {
        this.library = new Library();
    }

    public static void main(String[] args) throws IOException {
        loadBooksToLibrary(library);
        runSearchLoop(library);
    }

    private static void loadBooksToLibrary(Library library) throws IOException {
      library = new Library();
        // String excelFilePath = System.getProperty("user.dir") + File.separator + "/Data/books.csv";
        String fileName = "books.csv";
        String[] parts = fileName.split("\\.");
        String extension = parts[1];
        String excelFilePath = Paths.get(System.getProperty("user.dir"), "Data", fileName).toString();
        //library.batchUploadBooks(excelFilePath);


        BatchUploaderFactory factory = new FileFormatBatchUploaderFactory();
        BookBatchUploaders uploader = factory.createUploader(library,extension);
        uploader.batchUploadBooks(library,excelFilePath);
    }

    private static void runSearchLoop(Library library) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter search criteria (title, author, ISBN, or 'advanced' for more filters, leave empty to exit): ");
            String criteria = scanner.nextLine();
            if (criteria.isEmpty()) {
                break;
            }
            if ("advanced".equalsIgnoreCase(criteria)) {
                Map<String, String> searchCriteria = getAdvancedSearchCriteria(scanner);
                List<Book> filteredResults = library.searchBooks(searchCriteria);
                displaySearchResults(filteredResults);
            } else {
                List<Book> searchResults = library.searchBooks(criteria);
                displaySearchResults(searchResults);

                // Ask if the user wants to apply additional filters
                System.out.println("Would you like to apply additional filters? (yes/no): ");
                String additionalFilters = scanner.nextLine();
                if (additionalFilters.equalsIgnoreCase("yes")) {
                    Map<String, String> searchCriteria = getAdvancedSearchCriteria(scanner);
                    searchCriteria.put("basic", criteria); // Include the initial basic search criteria
                    List<Book> filteredResults = library.searchBooks(searchCriteria);
                    displaySearchResults(filteredResults);
                }
            }

            System.out.println("Would you like to continue searching? (yes/no): ");
            String continueSearch = scanner.nextLine();
            if (!continueSearch.equalsIgnoreCase("yes")) {
                break;
            }
        }

        scanner.close();
    }

    private static Map<String, String> getAdvancedSearchCriteria(Scanner scanner) {
        Map<String, String> searchCriteria = new HashMap<>();

        while (true) {
            System.out.println("Enter filter type (title, author, ISBN, genre, publicationDate) or 'done' to finish: ");
            String filterType = scanner.nextLine();
            if (filterType.equalsIgnoreCase("done")) {
                break;
            }
            System.out.println("Enter filter value: ");
            String filterValue = scanner.nextLine();
            searchCriteria.put(filterType, filterValue);
        }

        return searchCriteria;
    }


    private static  void displaySearchResults(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books match your criteria.");
            return;
        }  Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ": " + book.getTitle() + " by " + book.getAuthor() + " (ISBN: " + book.getISBN() + ")");
        }
        System.out.println("Enter the book number for more details, or leave empty to exit: ");
        String selection = scanner.nextLine();
        if (!selection.isEmpty()) {
            try {
                int bookNumber = Integer.parseInt(selection);
                if (bookNumber > 0 && bookNumber <= books.size()) {
                    Book selectedBook = books.get(bookNumber - 1); // Adjust for zero-based index
                    displayBookDetails(selectedBook);
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        }

    private static void displayBookDetails(Book book) {
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getISBN());
        System.out.println("Genre: " + book.getGenre());
        System.out.println("Publication Date: " + book.getPublicationDate());
        System.out.println("Available Copies: " + (book.getNumberOfCopies() > 0 ? book.getNumberOfCopies() : "Out of Stock"));
    }

}

