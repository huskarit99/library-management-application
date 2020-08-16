package com.example.librarymanagement.models;

public class Publisher {
    private int publisher_id;
    private String name;

    public Publisher(int publisher_id, String name){
        this.name = name;
        this.publisher_id = publisher_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getName() {
        return name;
    }

    public int getPublisher_id() {
        return publisher_id;
    }
}
