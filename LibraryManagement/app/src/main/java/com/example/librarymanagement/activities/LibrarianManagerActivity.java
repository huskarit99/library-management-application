package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.adapters.UserAdapter;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LibrarianManagerActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listLibrarian;
    ArrayList<User> userArrayList;
    UserAdapter userAdapter;
    public static final String LIBRARIAN = "LIBRARIAN";
    String url;
    TextView tvNotification;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_manager);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý thủ thư");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(this, R.layout.row_user, userArrayList);
        if (CheckConnect.isconnected(LibrarianManagerActivity.this)) {
            url = Server.getListUser + "?role_id=" + 2;
            getListUser(url);
            listLibrarian.setAdapter(userAdapter);
        } else {
            Toast.makeText(LibrarianManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<User> results = new ArrayList<>();
                for(User x : userArrayList){
                    if(x.getName().contains(newText) || String.valueOf(x.getUser_id()).contains(newText)){
                        results.add(x);
                    }
                }
                ((UserAdapter) listLibrarian.getAdapter()).update(results);
                listLibrarian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LibrarianManagerActivity.this, InformationLibrarianActivity.class);
                        intent.putExtra(LIBRARIAN, results.get(position));
                        startActivity(intent);
                    }
                });
                return false;
            }
        });

        listLibrarian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LibrarianManagerActivity.this, InformationLibrarianActivity.class);
                intent.putExtra(LIBRARIAN, userArrayList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_data) {
            Intent intent = new Intent(LibrarianManagerActivity.this, AddUserActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListUser(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(LibrarianManagerActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals("empty")) {
                            Toast.makeText(LibrarianManagerActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    userArrayList.add(new User(
                                            jsonObject.getInt("user_id"),
                                            jsonObject.getInt("role_id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("gender"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("birthday"),
                                            jsonObject.getString("address"),
                                            jsonObject.getString("image")
                                    ));
                                    userAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvNotification.setVisibility(View.VISIBLE);

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarLibrarianManager);
        listLibrarian = findViewById(R.id.listLibrarian);
        tvNotification = findViewById(R.id.tvNotification);
        searchView = findViewById(R.id.searchLibrarian);
    }
}