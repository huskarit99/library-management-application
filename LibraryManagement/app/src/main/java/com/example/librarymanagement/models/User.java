package com.example.librarymanagement.models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    int user_id;
    int role_id;
    String name;
    String gender;
    String email;
    String birthday;
    String address;
    String image;

    public User() {
    }

    public User(int user_id, int role_id, String name, String gender, String email, String birthday, String address, String image) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.image = image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(User o) {
        return 1;
    }
}
