package org.Scanner;

import java.io.IOException;

public class JsonBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(Library library,String filename) throws IOException {
        Library.batchUploadBooksJson(library,filename);
    }
}
