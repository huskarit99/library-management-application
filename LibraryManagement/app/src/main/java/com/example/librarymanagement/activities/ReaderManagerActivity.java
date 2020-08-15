package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReaderManagerActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listReader;
    ArrayList<User> userArrayList;
    UserAdapter userAdapter;
    public static final String READER = "READER";
    String url;
    TextView tvNotification;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_manager);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý độc giả");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(this, R.layout.row_user, userArrayList);
        if (CheckConnect.isconnected(ReaderManagerActivity.this)) {
            url = Server.getListUser + "?role_id=" + 1;
            getListUser(url);
            listReader.setAdapter(userAdapter);
        } else {
            Toast.makeText(ReaderManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
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
                ((UserAdapter) listReader.getAdapter()).update(results);

                listReader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ReaderManagerActivity.this, InformationReaderActivity.class);
                        intent.putExtra(READER, results.get(position));
                        startActivity(intent);
                    }
                });
                return false;
            }
        });
        listReader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ReaderManagerActivity.this, InformationReaderActivity.class);
                intent.putExtra(READER, userArrayList.get(position));
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
            Intent intent = new Intent(ReaderManagerActivity.this, AddUserActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListUser(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(ReaderManagerActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals("empty")) {
                            Toast.makeText(ReaderManagerActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                            tvNotification.setVisibility(View.VISIBLE);
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

    private void mapping() {
        toolbar = findViewById(R.id.toolbarReaderManager);
        listReader = findViewById(R.id.listReader);
        tvNotification = findViewById(R.id.tvNotification);
        searchView = findViewById(R.id.searchReader);
    }
}