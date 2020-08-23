package com.example.librarymanagement.models;


import java.io.Serializable;

public class Category implements Serializable {
    private int category_id;
    private String name;

    public Boolean areVariablesNull() {
        if (category_id == 0 || name == null){
            return false;
        }
        return true;
    }

    public Category(int category_id, String name) {
        this.category_id = category_id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

}
