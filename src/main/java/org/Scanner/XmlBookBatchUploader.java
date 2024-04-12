package org.Scanner;
import java.io.IOException;

public class XmlBookBatchUploader implements BookBatchUploaders {
    public void batchUploadBooks(Library library,String filename) throws IOException {
        Library.batchUploadBooksXml(library,filename);
    }
}

