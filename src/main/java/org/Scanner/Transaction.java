package org.Scanner;

import java.time.LocalDate;

public class Transaction {

    private String userId;
    private String bookISBN;
    private LocalDate borrowingDate;

    public Transaction(String userId, String bookISBN, LocalDate borrowingDate) {
        this.userId = userId;
        this.bookISBN = bookISBN;
        this.borrowingDate = borrowingDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "userId='" + userId + '\'' +
                ", bookISBN='" + bookISBN + '\'' +
                ", borrowingDate=" + borrowingDate +
                '}';
    }


}
