package com.example.librarymanagement.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUserId, edtPassword;
    private Button btnLogin;
    private SessionManager sessionManager;
    private Intent intent;
    private ImageButton btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();

        intent = new Intent(LoginActivity.this, HomeActivity.class);

        sessionManager = new SessionManager(this);

        if(sessionManager.check()){
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckConnect.isconnected(LoginActivity.this)) {
                    String userId = edtUserId.getText().toString();
                    String password = edtPassword.getText().toString();

                    if (userId.isEmpty() && password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Bạn chưa nhập thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userId.isEmpty() || password.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else {
                            doLogin(userId, password);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Thiết bị không kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Bạn có muốn thoát?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    private void mapping() {
        edtUserId = findViewById(R.id.edtUserId);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);

    }

    private void doLogin(final String userId, final String password) {
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Server.getUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("empty")) {
                            Toast.makeText(getApplication(), "Thông tin đăng nhập không hợp lệ.", Toast.LENGTH_LONG).show();
                        } else {
                            int role = Integer.parseInt(response);
                            sessionManager.setLogin(true);
                            sessionManager.setRole(role);
                            sessionManager.setUser(Integer.parseInt(userId));
                            finish();
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Lỗi hệ thống", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("password", password);
                return params;
            }
        };
        queue.add(postRequest);
    }
}