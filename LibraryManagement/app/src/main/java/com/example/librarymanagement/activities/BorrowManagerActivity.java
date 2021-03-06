package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.librarymanagement.R;

public class BorrowManagerActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_manager);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý thẻ mượn");
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
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_data){
            Intent intent = new Intent(BorrowManagerActivity.this, AddBillActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarBorrowedManager);
    }
}