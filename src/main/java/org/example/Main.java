package org.example;
import java.util.ArrayList;
import java.util.List;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Sample list of existing books
        List<Book> existingBooks = new ArrayList<>();
        existingBooks.add(new Book("Title1", "Author1", "1234567890", "Genre1", null, 0));
        existingBooks.add(new Book("Title2", "Author2", "0987654321", "Genre2", null, 0));

        // Check if ISBN is unique
        String newISBN = "9876543210";
        boolean isUnique = BookManager.isISBNUnique(newISBN, existingBooks);
        System.out.println("Is ISBN " + newISBN + " unique? " + isUnique); // Output: true
    }
}