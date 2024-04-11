package org.Scanner;

public class CsvBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(String filename) {
        Library.batchUploadBooks(filename);
    }
}

