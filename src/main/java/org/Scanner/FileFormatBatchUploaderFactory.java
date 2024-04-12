package org.Scanner;

public class FileFormatBatchUploaderFactory implements BatchUploaderFactory {
    public BookBatchUploaders createUploader(Library library,String format) {
        if (format.equalsIgnoreCase("json")) {
            return new JsonBookBatchUploader();
        } else if (format.equalsIgnoreCase("csv")) {
            return new CsvBookBatchUploader();
        } else if (format.equalsIgnoreCase("xml")) {
            return new XmlBookBatchUploader();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + format);
        }
    }


}
