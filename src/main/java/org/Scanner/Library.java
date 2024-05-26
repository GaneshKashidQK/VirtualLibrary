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
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Library {

    private List<Book> books;
    private int booksAdded;
    private int skippedDuplicates;

    public Library() {
        this.books = new ArrayList<>();
        this.booksAdded = 0;
        this.skippedDuplicates = 0;
    }



    public   void addBook(Book book) {
        books.add(book);
    }

    public   boolean isISBNUnique(String ISBN) {
        for (Book book : books) {
            if (book.getISBN().equals(ISBN)) {
                return false;
            }
        }
        return true;
    }

    public static void batchUploadBooks(Library library, String filename) {
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

                if (library.isISBNUnique(ISBN)) {
                    library.addBook(new Book(title, author, ISBN, genre, publicationDate, numberOfCopies));
                    library.booksAdded++;
                } else {
                    library.skippedDuplicates++;
                }
            }

            System.out.println("Books added: " + library.booksAdded);
            System.out.println("Skipped due to duplicate ISBNs: " + library.skippedDuplicates);

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void batchUploadBooksJson(Library library, String filename) throws IOException {
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

                if (library.isISBNUnique(isbn)) {
                    library.addBook(new Book(title, author, isbn, genre, publicationDate, numOfCopies));
                    library.booksAdded++;
                } else {
                    library.skippedDuplicates++;
                }
                System.out.println("Title: " + title + ", Author: " + author + ", ISBN: " + isbn +
                        ", Genre: " + genre + ", Publication Date: " + publicationDate +
                        ", Number of Copies: " + numOfCopies);
            }

            System.out.println("Books added: " + library.booksAdded);
            System.out.println("Skipped due to duplicate ISBNs: " + library.skippedDuplicates);

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public static void batchUploadBooksXml(Library library, String filename) throws IOException {
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
                if (library.isISBNUnique(isbn)) {
                    library.addBook(new Book(title, author, isbn, genre, publicationDate, numOfCopies));
                    library.booksAdded++;
                } else {
                    library.skippedDuplicates++;
                }

                System.out.println("Books added: " + library.booksAdded);
                System.out.println("Skipped due to duplicate ISBNs: " + library.skippedDuplicates);

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


    public List<Book> searchBooks(Map<String, String> searchCriteria) {
        return books.stream()
                .filter(book -> matchesCriteria(book, searchCriteria))
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Book book, Map<String, String> searchCriteria) {
        for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
            switch (entry.getKey()) {
                case "title":
                    if (!book.getTitle().contains(entry.getValue())) return false;
                    break;
                case "author":
                    if (!book.getAuthor().contains(entry.getValue())) return false;
                    break;
                case "ISBN":
                    if (!book.getISBN().contains(entry.getValue())) return false;
                    break;
                case "genre":
                    if (!book.getGenre().equals(entry.getValue())) return false;
                    break;
                case "publicationDate":
                    if (!book.getPublicationDate().toString().contains(entry.getValue())) return false;
                    break;
                default:
                    return false;
            }
        }
        return true;
    }




}
