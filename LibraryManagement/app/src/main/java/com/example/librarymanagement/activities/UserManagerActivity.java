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

public class UserManagerActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listUser;
    ArrayList<User> userArrayList;
    UserAdapter userAdapter;
    public static final String READER ="READER";
    public static final String LIBRARIAN ="LIBRARIAN";
    SessionManager sessionManager;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_manager);
        mapping();
        sessionManager = new SessionManager(this);
        setSupportActionBar(toolbar);
        if(sessionManager.getRole()==3){
            getSupportActionBar().setTitle("Quản lý thủ thư");
        }else if(sessionManager.getRole()==2){
            getSupportActionBar().setTitle("Quản lý độc giả");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(this, R.layout.row_user, userArrayList);
        if(CheckConnect.isconnected(UserManagerActivity.this)){
            if(sessionManager.getRole()==3){
                url= Server.getListUser+"?role_id="+2;
                getListUser(url);
                listUser.setAdapter(userAdapter);

            }else if(sessionManager.getRole()==2){
                url= Server.getListUser+"?role_id="+1;
                getListUser(url);
                listUser.setAdapter(userAdapter);
            }
        }else{
            Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
        }

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sessionManager.getRole()==3){
                    Intent intent = new Intent(UserManagerActivity.this, InformationLibrarianActivity.class);
                    intent.putExtra(LIBRARIAN,userArrayList.get(position));
                    startActivity(intent);

                }else if(sessionManager.getRole()==2){
                    Intent intent = new Intent(UserManagerActivity.this, InformationReaderActivity.class);
                    intent.putExtra(READER,userArrayList.get(position));
                    startActivity(intent);
                }
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
        if(item.getItemId() == R.id.add_data){
            Intent intent = new Intent(UserManagerActivity.this, AddUserActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListUser(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(UserManagerActivity.this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.equals("empty")){
                            Toast.makeText(UserManagerActivity.this, "Danh sách trống",Toast.LENGTH_SHORT).show();
                        }else{
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
                Toast.makeText(UserManagerActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarReaderManager);
        listUser = findViewById(R.id.listReader);
    }
}