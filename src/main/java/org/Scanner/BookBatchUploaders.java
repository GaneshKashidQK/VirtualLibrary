package org.Scanner;

import java.io.IOException;

public interface BookBatchUploaders {
    void batchUploadBooks(Library library,String filename) throws IOException;
}
