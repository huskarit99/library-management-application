package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.Server;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edtName, edtIdUser, edtBirthDay, edtEmail, edtAddress;
    String user_id, name, role, gender, email, birthday, address, password, image;
    RadioButton radioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm độc giả");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        gender = radioButton.getText().toString();


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(CheckConnect.isconnected(AddUserActivity.this)){
            if(item.getItemId() == R.id.save_data){
                user_id = edtIdUser.getText().toString();
                role ="1";
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                birthday = edtBirthDay.getText().toString();
                address = edtAddress.getText().toString();
                password = "123456";
                image = "123";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addUser,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AddUserActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AddUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userid", user_id);
                            params.put("role", role);
                            params.put("name", name);
                            params.put("gender", gender);
                            params.put("email", email);
                            params.put("birthday", birthday);
                            params.put("address", address);
                            params.put("password", password);
                            params.put("image", image);
                            return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        }else{
            Toast.makeText(AddUserActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        radioGroup = findViewById(R.id.radioGroup);
        toolbar = findViewById(R.id.toolbarAddUser);
        edtName = findViewById(R.id.edtName);
        edtIdUser = findViewById(R.id.edtIdUser);
        edtBirthDay = findViewById(R.id.edtBirthday);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
    }
}