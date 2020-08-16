package com.example.librarymanagement.models;

import android.app.NotificationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.text.Normalizer;

public class Book implements Serializable {
    private int book_id;
    private int category_id;
    private int publisher_id;
    private String name;
    private int publicationYear;
    private int amount;
    private String image;
    private String authors;
    private String category;
    private String publisher;

    public Book(int book_id, int category_id, int publisher_id, String name, int publicationYear, String image, int amount){
        this.book_id = book_id;
        this.category_id = category_id;
        this.publisher_id = publisher_id;
        this.name = name;
        this.publicationYear = publicationYear;
        this.amount = amount;
        this.image = image;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getBook_id() {
        return book_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
