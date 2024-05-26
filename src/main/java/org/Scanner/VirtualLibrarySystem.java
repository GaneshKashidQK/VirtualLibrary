package org.Scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class VirtualLibrarySystem {
    private static Library library;
    public VirtualLibrarySystem() {
        this.library = new Library();
    }

    public static void main(String[] args) throws IOException {
        loadBooksToLibrary(library);
        runLibrarySystem(library);
    }
    private static void runLibrarySystem(Library library) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 'search' to search books, 'borrow' to borrow a book, or 'exit' to quit:");
            String command = scanner.nextLine();
            if ("exit".equalsIgnoreCase(command)) {
                break;
            } else if ("search".equalsIgnoreCase(command)) {
                runSearchLoop(library, scanner);
            } else if ("borrow".equalsIgnoreCase(command)) {
                borrowBookByISBN(library, scanner);
            }else if ("viewlog".equalsIgnoreCase(command)) {
                viewTransactionLog(library);
            }
        }
        scanner.close();
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

    private static void runSearchLoop(Library library, Scanner scanner) {
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
    private static boolean validateISBN(String ISBN) {
        // Simple validation for ISBN-10 or ISBN-13 formats
        return ISBN.matches("\\d{10}") || ISBN.matches("\\d{13}");
    }

    private static void borrowBookByISBN(Library library, Scanner scanner) {
        while (true) {
            System.out.println("Enter your user ID:");
            String userId = scanner.nextLine();

            System.out.println("Enter the ISBN of the book you wish to borrow:");
            String ISBN = scanner.nextLine();
            if (!validateISBN(ISBN)) {
                System.out.println("Invalid ISBN format. Please try again.");
                continue;
            }

            Optional<Book> bookOptional = library.findBookByISBN(ISBN);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                System.out.println("You have selected: " + book.getTitle() + " by " + book.getAuthor());
                System.out.println("Do you want to proceed with borrowing this book? (yes/no):");
                String confirmation = scanner.nextLine();
                if ("yes".equalsIgnoreCase(confirmation)) {
                    if (library.borrowBook(userId, ISBN)) {
                        System.out.println("Book borrowed successfully! Remaining copies: " + book.getNumberOfCopies());
                        break;
                    } else {
                        System.out.println("ALERT: The book titled '" + book.getTitle() + "' is currently Out of Stock. Unable to proceed with borrowing.");
                        offerRetryOptions(scanner);
                    }
                } else {
                    System.out.println("Borrowing process cancelled.");
                    offerRetryOptions(scanner);
                }
            } else {
                System.out.println("Book with ISBN " + ISBN + " not found.");
                offerRetryOptions(scanner);
            }
        }
    }

    private static void offerRetryOptions(Scanner scanner) {
        while (true) {
            System.out.println("Enter 'menu' to return to the main menu or 'search' to search for another book:");
            String option = scanner.nextLine();
            if ("menu".equalsIgnoreCase(option)) {
                return;
            } else if ("search".equalsIgnoreCase(option)) {
                runSearchLoop(library, scanner);
                return;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewTransactionLog(Library library) {
        List<Transaction> transactionLog = library.getTransactionLog();
        if (transactionLog.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactionLog) {
                System.out.println(transaction);
            }
        }

    }
}

