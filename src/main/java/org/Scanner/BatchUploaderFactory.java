package org.Scanner;

public interface BatchUploaderFactory {
    BookBatchUploaders createUploader(String format);
}
