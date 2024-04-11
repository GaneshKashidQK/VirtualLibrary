package org.Scanner;

import java.io.IOException;

public interface BookBatchUploaders {
    void batchUploadBooks(String filename) throws IOException;
}
