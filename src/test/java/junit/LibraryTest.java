package junit;

import org.Scanner.Book;
import org.Scanner.Library;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private static Library library;

    String date = String.valueOf(new Date());

    @BeforeEach
    public void setUp() {
        library = new Library();
        library.addBook(new Book("Test Title", "Test Author", "1234567890", "Fiction", date, 5));
    }

    @Test
    public void testSearchBooksByTitle() {
        List<Book> results = library.searchBooks("Test Title");
        assertEquals(1, results.size());
        assertEquals("Test Title", results.get(0).getTitle());
    }

    @Test
    public void testSearchBooksByAdvancedCriteria() {
        Map<String, String> criteria = new HashMap<>();
        criteria.put("title", "Test Title");
        criteria.put("author", "Test Author");

        List<Book> results = library.searchBooks(criteria);
        assertEquals(1, results.size());
        assertEquals("Test Title", results.get(0).getTitle());
        assertEquals("Test Author", results.get(0).getAuthor());
    }

    @Test
    public void testBatchUploadBooks() throws IOException {
        Library.batchUploadBooks(library,"books.csv");
        assertFalse(library.searchBooks("Test Title").isEmpty());
    }

    @Test
    public void testBatchUploadBooksJson() throws IOException {
        Library.batchUploadBooksJson(library,"books.json");
        assertFalse(library.searchBooks("Test Title").isEmpty());
    }

    @Test
    public void testBatchUploadBooksXml() throws IOException {
        Library.batchUploadBooksXml(library,"books.xml");
        assertFalse(library.searchBooks("Test Title").isEmpty());
    }
}

