package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.squareup.picasso.Picasso;

public class InformationUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvNameUser, tvIdUser, tvBirthday, tvGender, tvEmail, tvAddress;
    ImageView imgAvatar;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_user);
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
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(HomeActivity.USER);
        if(user!=null){
            tvNameUser.setText(user.getName());
            tvIdUser.setText(String.valueOf(user.getUser_id()));
            tvBirthday.setText(user.getBirthday());
            tvGender.setText(user.getGender());
            tvEmail.setText(user.getEmail());
            tvAddress.setText(user.getAddress());
            user.setImage(user.getImage().replace("p:", "ps:"));
            Picasso.get().load(user.getImage()).into(imgAvatar);
        }else{
            Toast.makeText(InformationUserActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
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