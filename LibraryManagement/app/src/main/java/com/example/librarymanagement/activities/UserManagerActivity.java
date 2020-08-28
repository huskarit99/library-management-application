package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.librarymanagement.R;
import com.example.librarymanagement.adapters.UserAdapter;
import com.example.librarymanagement.models.NormalizeString;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.GetData;
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
    public static final String INFO_USER = "INFO_USER";
    String role;
    SearchView searchView;
    GetData getData, getDataRefresh;
    SessionManager sessionManager;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        getData = new GetData(this);
        sessionManager = new SessionManager(this);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(this, R.layout.row_user, userArrayList);
        if (sessionManager.getRole() == 3) {
            Intent intent = getIntent();
            role = intent.getStringExtra("LIBRARIAN MANAGER");
            if (role.equals("librarian manager")) {
                getSupportActionBar().setTitle("Quản lý thủ thư");
                if (CheckConnect.isconnected(UserManagerActivity.this)) {
                    if (getData.getListLibrarian().equals("")) {
                        Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray response = new JSONArray(getData.getListLibrarian());
                            for (int i = 0; i < response.length(); i++) {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listUser.setAdapter(userAdapter);
                } else {
                    Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            } else {
                getSupportActionBar().setTitle("Quản lý độc giả");
                if (CheckConnect.isconnected(UserManagerActivity.this)) {
                    if (getData.getListReader().equals("")) {
                        Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray response = new JSONArray(getData.getListReader());
                            for (int i = 0; i < response.length(); i++) {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listUser.setAdapter(userAdapter);
                } else {
                    Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sessionManager.getRole() == 2) {
            getSupportActionBar().setTitle("Quản lý độc giả");
            if (CheckConnect.isconnected(UserManagerActivity.this)) {
                if (getData.getListReader().equals("")) {
                    Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray response = new JSONArray(getData.getListReader());
                        for (int i = 0; i < response.length(); i++) {
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listUser.setAdapter(userAdapter);
            } else {
                Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<User> results = new ArrayList<>();
                for (User x : userArrayList) {
                    String name = new NormalizeString(x.getName()).getNormalizeString();
                    String normalizeNewText = new NormalizeString(newText).getNormalizeString();
                    if (name.contains(normalizeNewText) || String.valueOf(x.getUser_id()).contains(newText)) {
                        results.add(x);
                    }
                }
                ((UserAdapter) listUser.getAdapter()).update(results);

                listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(UserManagerActivity.this, InformationUserActivity.class);
                        intent.putExtra(INFO_USER, results.get(position));
                        startActivity(intent);
                    }
                });
                return false;
            }
        });
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserManagerActivity.this, InformationUserActivity.class);
                intent.putExtra(INFO_USER, userArrayList.get(position));
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataRefresh = new GetData(UserManagerActivity.this);
                userArrayList.clear();
                if (sessionManager.getRole() == 3) {
                    Intent intent = getIntent();
                    role = intent.getStringExtra("LIBRARIAN MANAGER");
                    if (role.equals("librarian manager")) {
                        if (CheckConnect.isconnected(UserManagerActivity.this)) {
                            if (getDataRefresh.getListLibrarian().equals("")) {
                                Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray response = new JSONArray(getDataRefresh.getListLibrarian());
                                    for (int i = 0; i < response.length(); i++) {
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
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            listUser.setAdapter(userAdapter);
                            refreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (CheckConnect.isconnected(UserManagerActivity.this)) {
                            if (getDataRefresh.getListReader().equals("")) {
                                Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray response = new JSONArray(getDataRefresh.getListReader());
                                    for (int i = 0; i < response.length(); i++) {
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
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            listUser.setAdapter(userAdapter);
                            refreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (sessionManager.getRole() == 2) {
                    if (CheckConnect.isconnected(UserManagerActivity.this)) {
                        if (getDataRefresh.getListReader().equals("")) {
                            Toast.makeText(UserManagerActivity.this, "Không thể load dữ liệu", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONArray response = new JSONArray(getDataRefresh.getListReader());
                                for (int i = 0; i < response.length(); i++) {
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
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listUser.setAdapter(userAdapter);
                        refreshLayout.setRefreshing(false);
                    } else {
                        Toast.makeText(UserManagerActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
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
        if (item.getItemId() == R.id.add_data) {
            Intent intent = new Intent(UserManagerActivity.this, AddUserActivity.class);
            if(role.equals("librarian manager")){
                intent.putExtra("ADD", "add_librarian");
            }else{
                intent.putExtra("ADD", "add_reader");
            }

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarReaderManager);
        listUser = findViewById(R.id.listReader);
        searchView = findViewById(R.id.searchReader);
        refreshLayout = findViewById(R.id.swipe_refresh_layout);
    }
}