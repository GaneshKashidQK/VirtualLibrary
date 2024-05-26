package org.Scanner;

import java.io.IOException;

public class CsvBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(Library library,String filename) throws IOException {
        Library.batchUploadBooks(library,filename);
    }
}

