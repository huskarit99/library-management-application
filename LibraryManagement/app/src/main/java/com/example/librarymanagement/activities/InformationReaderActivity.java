package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.squareup.picasso.Picasso;

public class InformationReaderActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvNameReader, tvIdReader, tvBirthdayReader, tvGenderReader, tvEmailReader, tvAddressReader;
    ImageView imgAvatar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_reader);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin độc giả");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(ReaderManagerActivity.READER);
        if(user!=null){
            tvNameReader.setText(user.getName());
            tvIdReader.setText(String.valueOf(user.getUser_id()));
            tvBirthdayReader.setText(user.getBirthday());
            tvGenderReader.setText(user.getGender());
            tvEmailReader.setText(user.getEmail());
            tvAddressReader.setText(user.getAddress());
            Picasso.get().load(user.getImage()).into(imgAvatar);
        }else{
            Toast.makeText(InformationReaderActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit_data){
            Intent intent = new Intent(InformationReaderActivity.this, AddUserActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarInfoReader);
        tvNameReader = findViewById(R.id.tvNameReader);
        tvIdReader = findViewById(R.id.tvIdReader);
        tvBirthdayReader = findViewById(R.id.tvBirthdayReader);
        tvGenderReader = findViewById(R.id.tvGenderReader);
        tvEmailReader = findViewById(R.id.tvEmailReader);
        tvAddressReader = findViewById(R.id.tvAddressReader);
        imgAvatar = findViewById(R.id.imgAvatar);
    }
}