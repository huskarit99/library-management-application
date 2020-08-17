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

public class InformationLibrarianActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvNameLibrarian, tvIdLibrarian, tvBirthdayLibrarian, tvGenderLibrarian, tvEmailLibrarian, tvAddressLibrarian;
    ImageView imgAvatar;
    User user;
    public static final String LIBRARIAN = "LIBRARIAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_librarian);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin thủ thư");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(LibrarianManagerActivity.LIBRARIAN);
        if(user!=null){
            tvNameLibrarian.setText(user.getName());
            tvIdLibrarian.setText(String.valueOf(user.getUser_id()));
            tvBirthdayLibrarian.setText(user.getBirthday());
            tvGenderLibrarian.setText(user.getGender());
            tvEmailLibrarian.setText(user.getEmail());
            tvAddressLibrarian.setText(user.getAddress());
            Picasso.get().load(user.getImage()).into(imgAvatar);
        }else{
            Toast.makeText(InformationLibrarianActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(InformationLibrarianActivity.this, EditLibrarianActivity.class);
            intent.putExtra(LIBRARIAN,user);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarInfoLibrarian);
        tvNameLibrarian = findViewById(R.id.tvNameLibrarian);
        tvIdLibrarian = findViewById(R.id.tvIdLibrarian);
        tvBirthdayLibrarian = findViewById(R.id.tvBirthdayLibrarian);
        tvGenderLibrarian = findViewById(R.id.tvGenderLibrarian);
        tvEmailLibrarian = findViewById(R.id.tvEmailLibrarian);
        tvAddressLibrarian = findViewById(R.id.tvAddressLibrarian);
        imgAvatar = findViewById(R.id.imgAvatar);
    }
}