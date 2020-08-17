package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.librarymanagement.R;

public class BillActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvUserName, tvBorrowDate, tvGiveBackDate, tvBookName;
    EditText edtIdUser, edtIdBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm thẻ mượn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_data){

        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarBill);
        edtIdUser = findViewById(R.id.edtIdUser);
        tvUserName = findViewById(R.id.tvUserName);
        tvBorrowDate = findViewById(R.id.tvBorrowDate);
        tvGiveBackDate = findViewById(R.id.tvGiveBackDate);
        edtIdBook = findViewById(R.id.edtIdBook);
        tvBookName = findViewById(R.id.tvBookName);
    }

}