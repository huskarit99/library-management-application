package com.example.librarymanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Author;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.models.BookOfAuthors;
import com.example.librarymanagement.models.BorrowedBook;
import com.example.librarymanagement.models.Category;
import com.example.librarymanagement.models.Publisher;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.models.VolleySingleton;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<User> listUser;
    ArrayList<Book> listBook;
    ArrayList<Author> listAuthor;
    ArrayList<Category> listCategory;
    ArrayList<Publisher> listPublisher;
    ArrayList<BookOfAuthors> listBookOfAuthors;
    ArrayList<BorrowedBook> listBorrowedBook;

    Map<Integer, String> mappingCategory;
    Map<Integer, String> mappingPublisher;
    Map<Integer, String> mappingAuthor;

    SessionManager sessionManage;

    Runnable myRunnable;
    public static Handler myHandler = new Handler();
    private static final int TIME_TO_WAIT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManage = new SessionManager(this);

        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (listCategory.size() == 0 || listAuthor.size() == 0 || listPublisher.size() == 0 || listBookOfAuthors.size() == 0
                        || listUser.size() == 0 || listBook.size() == 0 || listBorrowedBook.size() == 0 || checkVariablesNull() == false) {
                    System.out.println(listBorrowedBook.size());
                    restart();
                } else {
                    sessionManage.setBookOfAuthors(listBookOfAuthors);
                    sessionManage.setBorrowedBook(listBorrowedBook);
                    sessionManage.setPublisher(listPublisher);
                    sessionManage.setInfoAuthor(listAuthor);
                    sessionManage.setInfoCategory(listCategory);
                    sessionManage.setInfoUser(listUser);
                    sessionManage.setInfoBook(listBook);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        start();
    }

    public void start() {
        getInfoCategory();
        getInfoAuthor();
        getInfoPublisher();
        getInfoBookOfAuthors();
        getInfoUser();
        getAllBook();
        getBorrowedBook();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }

    public void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    public void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }

    private Boolean checkVariablesNull(){
        for (int i = 0; i < listBorrowedBook.size(); i++)
            if (listBorrowedBook.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listBook.size(); i++)
            if (listBook.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listBookOfAuthors.size(); i++)
            if (listBookOfAuthors.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listPublisher.size(); i++)
            if (listPublisher.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listAuthor.size(); i++)
            if (listAuthor.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listCategory.size(); i++)
            if (listCategory.get(i).areVariablesNull() == false) return false;
        for (int i = 0; i < listUser.size(); i++)
            if (listUser.get(i).areVariablesNull() == false) return false;
        return true;
    }

    private void getAllBook(){
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listBook = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.GETALLBOOKS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listBook.add(new Book(
                                        jsonObject.getInt("book_id"),
                                        jsonObject.getInt("category_id"),
                                        jsonObject.getInt("publisher_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("publicationYear"),
                                        jsonObject.getString("image"),
                                        jsonObject.getInt("amount")
                                ));
                                int category_id = listBook.get(i).getCategory_id();
                                listBook.get(i).setCategory(mappingCategory.get(category_id));
                                int publisher_id = listBook.get(i).getPublisher_id();
                                listBook.get(i).setPublisher(mappingPublisher.get(publisher_id));

                                String bookOfAuthors = "";
                                for (int j = 0; j < listBookOfAuthors.size(); j++){
                                    if (listBookOfAuthors.get(j).getBook_id() == listBook.get(i).getBook_id()){
                                        if (bookOfAuthors != "")
                                            bookOfAuthors += " ,";
                                        bookOfAuthors += mappingAuthor.get(listBookOfAuthors.get(j).getAuthor_id());
                                    }
                                }
                                listBook.get(i).setAuthors(bookOfAuthors);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Không lấy được dữ kiệu người dùng",Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getInfoUser() {
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listUser = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.GETINFOUSER, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listUser.add(new User(
                                        jsonObject.getInt("user_id"),
                                        jsonObject.getInt("role_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("gender"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("birthday"),
                                        jsonObject.getString("address"),
                                        jsonObject.getString("image")
                                ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Không lấy được dữ kiệu người dùng",Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getInfoBookOfAuthors() {
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listBookOfAuthors = new ArrayList<>();
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLBOOKOFAUTHORS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listBookOfAuthors.add(new BookOfAuthors(
                                        Integer.parseInt(jsonObject.getString("book_id")),
                                        Integer.parseInt(jsonObject.getString("author_id"))
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoPublisher() {
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listPublisher = new ArrayList<>();
        mappingPublisher = new HashMap<Integer, String>();
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLPUBLISHERS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listPublisher.add(new Publisher(
                                        Integer.parseInt(jsonObject.getString("publisher_id")),
                                        jsonObject.getString("name")
                                ));
                                mappingPublisher.put(listPublisher.get(i).getPublisher_id(), listPublisher.get(i).getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoCategory(){
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listCategory = new ArrayList<>();
        mappingCategory = new HashMap<Integer, String>();
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLCATEGORIES, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listCategory.add(new Category(
                                        Integer.parseInt(jsonObject.getString("category_id")),
                                        jsonObject.getString("name")
                                ));
                                mappingCategory.put(listCategory.get(i).getCategory_id(), listCategory.get(i).getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoAuthor(){
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listAuthor = new ArrayList<>();
        mappingAuthor = new HashMap<Integer, String>();
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLAUTHORS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listAuthor.add(new Author(
                                        Integer.parseInt(jsonObject.getString("author_id")),
                                        jsonObject.getString("name")
                                ));
                                mappingAuthor.put(listAuthor.get(i).getAuthor_id(), listAuthor.get(i).getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getBorrowedBook() {
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        listBorrowedBook = new ArrayList<BorrowedBook>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Server.GETBORROWEDBOOK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listBorrowedBook.add(new BorrowedBook(
                                        jsonObject.getInt("bill_id"),
                                        jsonObject.getInt("user_id"),
                                        jsonObject.getInt("book_id"),
                                        jsonObject.getString("dateOfBorrowed"),
                                        jsonObject.getString("dateOfPurchase"),
                                        jsonObject.getInt("state")
                                ));
                                for (int j = 0; j < listBook.size(); j++){
                                    if (listBook.get(j).getBook_id() == listBorrowedBook.get(i).getBook_id()){
                                        listBorrowedBook.get(i).setName(listBook.get(j).getName());
                                        listBorrowedBook.get(i).setImage(listBook.get(j).getImage());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, null);
        requestQueue.add(jsonArrayRequest);
    }
}
