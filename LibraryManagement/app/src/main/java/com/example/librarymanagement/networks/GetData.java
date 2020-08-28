package com.example.librarymanagement.networks;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class GetData {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String NAME ="DATA";
    private static final String KEY_LIBRARIANS = "key_users";
    private static final String KEY_READERS ="key_readers";
    private static final String KEY_BOOKS = "key_books";
    private static final String KEY_AUTHORS = "key_authors";
    private static final String KEY_CATEGORIES ="key_categories";
    private static final String KEY_PUBLISHERS = "key_publishers";
    private static final String KEY_BORROWED_BOOKS = "key_borrowed_books";
    private static final String KEY_BOOK_OF_AUTHOR = "key_book_of_author";
    private static final String KEY_RULE ="key_rule";

    public GetData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // get list librarians
    public String getListReader(){
        return sharedPreferences.getString(KEY_READERS, "");
    }
    public void setListReader(JSONArray jsonArray){
        editor.putString(KEY_READERS, jsonArray.toString());
        editor.commit();
    }

    // get list librarians
    public String getListLibrarian(){
        return sharedPreferences.getString(KEY_LIBRARIANS, "");
    }
    public void setListLibrarian(JSONArray jsonArray){
        editor.putString(KEY_LIBRARIANS, jsonArray.toString());
        editor.commit();
    }

    // get list books
    public String getListBook(){
        return sharedPreferences.getString(KEY_BOOKS,"");
    }
    public void setListBook(JSONArray jsonArray){
        editor.putString(KEY_BOOKS, jsonArray.toString());
        editor.commit();
    }

    // get list authors
    public String getListAuthor(){
        return sharedPreferences.getString(KEY_AUTHORS, "");
    }
    public void setListAuthor(JSONArray jsonArray){
        editor.putString(KEY_AUTHORS, jsonArray.toString());
        editor.commit();
    }

    // get list categories
    public String getListCategory(){
        return sharedPreferences.getString(KEY_CATEGORIES, "");
    }
    public void setListCategory(JSONArray jsonArray){
        editor.putString(KEY_CATEGORIES, jsonArray.toString());
        editor.commit();
    }

    // get list publishers
    public String getListPublisher(){
        return sharedPreferences.getString(KEY_PUBLISHERS, "");
    }
    public void setListPublisher(JSONArray jsonArray){
        editor.putString(KEY_PUBLISHERS, jsonArray.toString());
        editor.commit();
    }

    // get list borrowed books
    public String getListBorrowedBook(){
        return sharedPreferences.getString(KEY_BORROWED_BOOKS, "");
    }
    public void setListBorrowedBook(JSONArray jsonArray){
        editor.putString(KEY_BORROWED_BOOKS, jsonArray.toString());
        editor.commit();
    }

    // get list book of authors
    public String getListBookOfAuthor(){
        return sharedPreferences.getString(KEY_BOOK_OF_AUTHOR, "");
    }
    public void setListBookOfAuthor(JSONArray jsonArray){
        editor.putString(KEY_BOOK_OF_AUTHOR, jsonArray.toString());
        editor.commit();
    }

    // get rule
    public String getRule(){
        return sharedPreferences.getString(KEY_RULE, "");
    }
    public void setRule(JSONArray jsonArray){
        editor.putString(KEY_RULE, jsonArray.toString());
        editor.commit();
    }


    public void clearData(){
        editor.clear();
        editor.commit();
    }
}
