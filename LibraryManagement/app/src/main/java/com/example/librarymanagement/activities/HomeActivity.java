package com.example.librarymanagement.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbarHome;
    LinearLayout linearLogout,linearInfo;
    SessionManager sessionManager;
    CardView cardViewLibrarian, cardViewAdmin;
    static boolean count;
    User user;
    ArrayList<User> listUser;
    int user_id;
    String url;
    public static final String USER = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        sessionManager = new SessionManager(this);


        setSupportActionBar(toolbarHome);
        getSupportActionBar().setTitle("Trang chủ");

        if(sessionManager.getRole()==1){
            cardViewLibrarian.setVisibility(View.GONE);
            cardViewAdmin.setVisibility(View.GONE);
        }else{
            if(sessionManager.getRole()==2){
                cardViewAdmin.setVisibility(View.GONE);
            }
        }

        getInfoUser();

        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Bạn có muốn đăng xuất?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent  intent = new Intent(HomeActivity.this, LoginActivity.class);
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


    }

    private void getInfoUser() {
        sessionManager = new SessionManager(this);
        user_id=sessionManager.getUser();
        url= Server.getInfoUser+"?user_id="+user_id;
        listUser = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
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

    @Override
    protected void onResume() {
        count = false;
        super.onResume();
    }

    @Override
    public void onBackPressed() {
       if(!count){
           count = true;
           Toast.makeText(HomeActivity.this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();
       }else{
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
        cardViewLibrarian = findViewById(R.id.cardViewLibrarian);
        cardViewAdmin = findViewById(R.id.cardViewAdmin);
        linearInfo = findViewById(R.id.lnInfo);
    }
}