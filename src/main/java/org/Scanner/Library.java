package org.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    static int booksAdded = 0;
    static int skippedDuplicates = 0;
    private static List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public static void addBook(Book book) {
        books.add(book);
    }

    public static boolean isISBNUnique(String ISBN) {
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                return false;
            }
        }
        return true;
    }

    public static void batchUploadBooks(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
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


    public static void batchUploadBooksJson(String filename) throws IOException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filename));

            for (JsonNode bookNode : rootNode) {
                String title = bookNode.get("title").asText();
                String author = bookNode.get("author").asText();
                String isbn = bookNode.get("isbn").asText();
                String genre = bookNode.get("genre").asText();
                String publicationDate = bookNode.get("publicationDate").asText();
                int numOfCopies = bookNode.get("numOfCopies").asInt();

                if (isISBNUnique(isbn)) {
                    addBook(new Book(title, author, isbn, genre, publicationDate, numOfCopies));
                    booksAdded++;
                } else {
                    skippedDuplicates++;
                }
                System.out.println("Title: " + title + ", Author: " + author + ", ISBN: " + isbn +
                        ", Genre: " + genre + ", Publication Date: " + publicationDate +
                        ", Number of Copies: " + numOfCopies);
            }

            System.out.println("Books added: " + booksAdded);
            System.out.println("Skipped due to duplicate ISBNs: " + skippedDuplicates);

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public static void batchUploadBooksXml(String filename) throws IOException {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));

            NodeList bookList = document.getElementsByTagName("book");
            for (int i = 0; i < bookList.getLength(); i++) {
                Element bookElement = (Element) bookList.item(i);
                String title = getChildElementTextContent(bookElement, "title");
                String author = getChildElementTextContent(bookElement, "author");
                String isbn = getChildElementTextContent(bookElement, "isbn");
                String genre = getChildElementTextContent(bookElement, "genre");
                String publicationDate = getChildElementTextContent(bookElement, "publicationDate");
                int numOfCopies = Integer.parseInt(getChildElementTextContent(bookElement, "numOfCopies"));
                if (isISBNUnique(isbn)) {
                    addBook(new Book(title, author, isbn, genre, publicationDate, numOfCopies));
                    booksAdded++;
                } else {
                    skippedDuplicates++;
                }

                System.out.println("Books added: " + booksAdded);
                System.out.println("Skipped due to duplicate ISBNs: " + skippedDuplicates);

            }
        } catch (Exception e) {
            throw new IOException("Error parsing XML file", e);
        }
    }

    private static String getChildElementTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return "";
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
