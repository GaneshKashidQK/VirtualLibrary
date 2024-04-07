package org.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {

    private List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean isISBNUnique(String ISBN) {
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                return false;
            }
        }
        return true;
    }

    public void batchUploadBooks(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int booksAdded = 0;
            int skippedDuplicates = 0;
            boolean isFirstLine = true; // Flag to skip the header line
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }
                String title = data[0];
                String author = data[1];
                String ISBN = data[2];
                String genre = data[3];
                String publicationDate = data[4];
                int numberOfCopies = Integer.parseInt(data[5]);

                if (isISBNUnique(ISBN)) {
                    addBook(new Book(title, author, ISBN, genre, publicationDate, numberOfCopies));
                    booksAdded++;
                } else {
                    skippedDuplicates++;
                }
            }

            System.out.println("Books added: " + booksAdded);
            System.out.println("Skipped due to duplicate ISBNs: " + skippedDuplicates);

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public List<Book> searchBooks(String criteria) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(criteria.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(criteria.toLowerCase()) ||
                    book.getISBN().toLowerCase().contains(criteria.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }


    // Display book details in CLI
    public void displayBookDetails(Book book) {
        System.out.println("------------------------------------");
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getISBN());
        System.out.println("Genre: " + book.getGenre());
        System.out.println("Publication Date: " + book.getPublicationDate());
    }

}
