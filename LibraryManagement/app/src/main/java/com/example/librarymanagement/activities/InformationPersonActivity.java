package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformationPersonActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvNameUser, tvIdUser, tvBirthday, tvGender, tvEmail, tvAddress;
    ImageView imgAvatar;
    User user;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_person);
        sessionManager = new SessionManager(this);
        mapping();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin cá nhân");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(CheckConnect.isconnected(this)){
            if(sessionManager.getInfoUser().equals("")){
                Toast.makeText(InformationPersonActivity.this, "Không thể load thông tin", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONArray response = new JSONArray(sessionManager.getInfoUser());
                    JSONObject jsonObject = response.getJSONObject(0);
                    tvNameUser.setText(jsonObject.getString("name"));
                    tvIdUser.setText(jsonObject.getString("user_id"));
                    tvBirthday.setText(jsonObject.getString("birthday"));
                    tvGender.setText(jsonObject.getString("gender"));
                    tvEmail.setText(jsonObject.getString("email"));
                    tvAddress.setText(jsonObject.getString("address"));
                    String image = jsonObject.getString("image").replace("p:", "ps:");
                    Picasso.get().load(image).into(imgAvatar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void mapping() {
        tvNameUser = findViewById(R.id.tvNameUser);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvGender = findViewById(R.id.tvGender);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        imgAvatar = findViewById(R.id.imgAvatar);
        toolbar=findViewById(R.id.toolbarInfoUser);

    }
}