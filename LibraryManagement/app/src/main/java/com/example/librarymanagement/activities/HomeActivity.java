package com.example.librarymanagement.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Author;
import com.example.librarymanagement.models.BookOfAuthors;
import com.example.librarymanagement.models.Category;
import com.example.librarymanagement.models.Publisher;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbarHome;
    LinearLayout linearLogout, linearInfo, linearReaderManager, linearLibrarianManager, linearRule, linearSearchBook, linearBookManager,
    linearBill;

    SessionManager sessionManager;
    CardView cardViewLibrarian, cardViewAdmin;
    ImageButton searchBook, bookManager;
    User user;
    String url;
    static boolean count;
    int user_id;

    ArrayList<User> listUser;
    ArrayList<Author> listAuthor;
    ArrayList<Category> listCategory;
    ArrayList<Publisher> listPublisher;
    ArrayList<BookOfAuthors> listBookOfAuthors;

    Map<Integer, String> mappingCategory;
    Map<Integer, String> mappingPublisher;
    Map<Integer, String> mappingAuthor;

    public static final String USER = "USER";
    public static final String BOOK = "BOOK";

    ArrayList<Book> listBook;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        sessionManager = new SessionManager(this);

        setSupportActionBar(toolbarHome);
        getSupportActionBar().setTitle("Trang chủ");

        if (sessionManager.getRole() == 1) {
            cardViewLibrarian.setVisibility(View.GONE);
            cardViewAdmin.setVisibility(View.GONE);
        } else {
            if (sessionManager.getRole() == 2) {
                cardViewAdmin.setVisibility(View.GONE);
            }
        }

        requestQueue = Volley.newRequestQueue(HomeActivity.this);
        getInfoUser();
        getAllBook();
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Bạn có muốn đăng xuất?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                sessionManager.clear();
                                startActivity(intent);
                                onDestroy();
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();

            }
        });
        linearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,InformationUserActivity.class);
                intent.putExtra(USER,user);
                startActivity(intent);
            }
        });
        linearReaderManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReaderManagerActivity.class);
                startActivity(intent);
            }
        });
        linearLibrarianManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LibrarianManagerActivity.class);
                startActivity(intent);
            }
        });
        linearRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChangeRuleActivity.class);
                startActivity(intent);
            }
        });

        linearSearchBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(BOOK, listBook);
                intent.putExtra("BEFORE", "SEARCH");
                startActivity(intent);
            }
        });

        linearBookManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra(BOOK, listBook);
                intent.putExtra("BEFORE", "MANAGEMENT");
                startActivity(intent);
            }
        });
        linearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getAllBook(){
        listBook = new ArrayList<>();
        getInfoCategory();
        getInfoAuthor();
        getInfoPublisher();
        getInfoBookOfAuthors();
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
                        Toast.makeText(HomeActivity.this,"Không lấy được dữ kiệu người dùng",Toast.LENGTH_SHORT).show();
                    }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getInfoUser() {
        sessionManager = new SessionManager(this);
        user_id=sessionManager.getUser();
        url= Server.GETINFOUSER+"?user_id="+user_id;
        listUser = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
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
                                user = listUser.get(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this,"Không lấy được dữ kiệu người dùng",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getInfoBookOfAuthors() {
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
                        Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoPublisher() {
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
                        Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoCategory(){
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
                        Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    private void getInfoAuthor(){
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
                        Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    @Override
    protected void onResume() {
        count = false;
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!count) {
            count = true;
            Toast.makeText(HomeActivity.this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }


    private void mapping() {
        toolbarHome = findViewById(R.id.toolbarHome);
        linearLogout = findViewById(R.id.lnLogout);
        linearInfo = findViewById(R.id.lnInfo);
        searchBook = findViewById(R.id.searchBook);
        bookManager = findViewById(R.id.bookManager);
        linearReaderManager = findViewById(R.id.lnReaderManager);
        linearLibrarianManager = findViewById(R.id.lnLibrarianManager);
        linearRule = findViewById(R.id.lnRule);
        linearSearchBook = findViewById(R.id.lnSearchBook);
        linearBookManager = findViewById(R.id.lnBookManager);
        linearBill = findViewById(R.id.lnBill);
        cardViewLibrarian = findViewById(R.id.cardViewLibrarian);
        cardViewAdmin = findViewById(R.id.cardViewAdmin);
    }
}