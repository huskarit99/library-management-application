package com.example.librarymanagement.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Author;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.GetData;
import com.example.librarymanagement.networks.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    GetData getData;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData = new GetData(this);
        progressBar = findViewById(R.id.pBLoading);


        if(CheckConnect.isconnected(this)){
            getListReader();
            getListLibrarian();
            getListBook();
            getListBookOfAuthor();
            getListPublisher();
            getListAuthor();
            getListCategory();
            getRule();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2300);
        }else{
            Toast.makeText(MainActivity.this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void getListAuthor(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLAUTHORS, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListAuthor(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListCategory(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLCATEGORIES, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListCategory(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListReader(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+1;
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListReader(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListLibrarian(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+2;
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListLibrarian(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListBook(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLBOOKS, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListBook(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListBorrowedBook(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETBORROWEDBOOK, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListBorrowedBook(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListBookOfAuthor(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLBOOKOFAUTHORS, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListBookOfAuthor(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListPublisher(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLPUBLISHERS, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListPublisher(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }
    private void getRule(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETRULE + "?id=1";
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setRule(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

}
