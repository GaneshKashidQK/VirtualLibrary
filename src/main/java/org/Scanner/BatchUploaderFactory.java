package org.Scanner;

public interface BatchUploaderFactory {
    BookBatchUploaders createUploader(Library library,String format);
}
