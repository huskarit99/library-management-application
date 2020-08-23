package com.example.librarymanagement.models;

import java.io.Serializable;

public class BookOfAuthors implements Serializable {
    private int author_id;
    private int book_id;

    public BookOfAuthors(int book_id, int author_id){
        this.author_id = author_id;
        this.book_id = book_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public Boolean areVariablesNull() {
        if (author_id == 0 || book_id == 0){
            return false;
        }
        return true;
    }
}
