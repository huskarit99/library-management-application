package com.example.librarymanagement.networks;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.librarymanagement.models.Author;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.models.BookOfAuthors;
import com.example.librarymanagement.models.BorrowedBook;
import com.example.librarymanagement.models.Category;
import com.example.librarymanagement.models.ObjectSerializer;
import com.example.librarymanagement.models.Publisher;
import com.example.librarymanagement.models.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class SessionManager {
    private static String TAG = SessionManager.class.getName();

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static int PRE_MODE = 1;
    private static final String NAME = "android_demo";
    private static final String KEY_LOGIN = "islogin";
    private static final String USERID = "userid";
    private static final String ROLE ="role";
    private static final String CATEGORIES = "categories";
    private static final String AUTHORS = "authors";
    private static final String PUBLISHERS = "publishers";
    private static final String USERS = "users";
    private static final String BOOKS = "books";
    private static final String BORROWEDBOOKS = "borrowedbooks";
    private static final String BOOKOFAUTHORS = "bookofauthors";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUserId(int userId){
        editor.putInt(USERID, userId);
        editor.commit();
    }

    public void setRole(int role){
        editor.putInt(ROLE, role);
        editor.commit();
    }

    public void setLogin(boolean isLogin){
        editor.putBoolean(KEY_LOGIN, isLogin);
        editor.commit();
    }

    public void setInfoCategory(ArrayList<Category> listCategory){
        try {
            editor.putString(CATEGORIES, ObjectSerializer.serialize(listCategory));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public void setInfoAuthor(ArrayList<Author> listCategory){
        try {
            editor.putString(AUTHORS, ObjectSerializer.serialize(listCategory));
        }catch (IOException e){
            e.printStackTrace();
        }                                                                                                 
        editor.commit();
    }

    public void setPublisher(ArrayList<Publisher> listPublisher){
        try {
            editor.putString(PUBLISHERS, ObjectSerializer.serialize(listPublisher));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public void setInfoUser(ArrayList<User> listUser){
        try {
            editor.putString(USERS, ObjectSerializer.serialize(listUser));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public void setInfoBook(ArrayList<Book> listBook){
        try {
            editor.putString(BOOKS, ObjectSerializer.serialize(listBook));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public void setBorrowedBook(ArrayList<BorrowedBook> listBorrowedBook){
        try {
            editor.putString(BORROWEDBOOKS, ObjectSerializer.serialize(listBorrowedBook));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public void setBookOfAuthors(ArrayList<BookOfAuthors> listBookOfAuthors){
        try {
            editor.putString(BOOKOFAUTHORS, ObjectSerializer.serialize(listBookOfAuthors));
        }catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();
    }

    public ArrayList<Category> getInfoCategory() {
        try {
            return (ArrayList<Category>) ObjectSerializer.deserialize(preferences.getString(CATEGORIES, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Author> getInfoAuthor(){
        try {
            return (ArrayList<Author>) ObjectSerializer.deserialize(preferences.getString(AUTHORS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Publisher> getInfoPublisher(){
        try {
            return (ArrayList<Publisher>) ObjectSerializer.deserialize(preferences.getString(PUBLISHERS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getInfoUser(){
        try {
            return (ArrayList<User>) ObjectSerializer.deserialize(preferences.getString(USERS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Book> getAllBook(){
        try {
            return (ArrayList<Book>) ObjectSerializer.deserialize(preferences.getString(BOOKS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<BorrowedBook> getBorrowedBook(){
        try {
            return (ArrayList<BorrowedBook>) ObjectSerializer.deserialize(preferences.getString(BORROWEDBOOKS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<BookOfAuthors> getBookOfAuthors(){
        try {
            return (ArrayList<BookOfAuthors>) ObjectSerializer.deserialize(preferences.getString(BOOKOFAUTHORS, ""));
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId(){
        return preferences.getInt(USERID, 0);
    }

    public int getRole(){
        return preferences.getInt(ROLE, 0);
    }

    public boolean check(){
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
