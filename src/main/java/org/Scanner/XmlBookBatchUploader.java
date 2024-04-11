package org.Scanner;
import java.io.IOException;

public class XmlBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(String filename) throws IOException {
        Library.batchUploadBooksXml(filename);
    }
}

