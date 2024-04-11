package org.Scanner;

import java.io.IOException;

public class JsonBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(String filename) throws IOException {
        Library.batchUploadBooksJson(filename);
    }
}
