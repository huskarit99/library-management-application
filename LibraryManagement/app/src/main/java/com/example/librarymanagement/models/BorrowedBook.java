package com.example.librarymanagement.models;

import java.io.Serializable;

public class BorrowedBook implements Serializable {
    private int bill_id;
    private int user_id;
    private int book_id;
    private String dateOfBorrowed;
    private String dateOfPurchase;
    private int state;

    private String name;
    private String image;

    public BorrowedBook(int bill_id, int user_id, int book_id, String dateOfBorrowed, String dateOfPurchase, int state){
        this.bill_id = bill_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.dateOfBorrowed = dateOfBorrowed;
        this.dateOfPurchase = dateOfPurchase;
        this.state = state;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public void setDateOfBorrowed(String dateOfBorrowed) {
        this.dateOfBorrowed = dateOfBorrowed;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDateOfBorrowed() {
        return dateOfBorrowed;
    }

    public int getBook_id() {
        return book_id;
    }

    public int getBill_id() {
        return bill_id;
    }

    public int getState() {
        return state;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }
}
