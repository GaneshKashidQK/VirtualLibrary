package org.Scanner;

public class CsvBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(Library library,String filename) {
        Library.batchUploadBooks(library,filename);
    }
}

