package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.librarymanagement.R;

public class InformationBillActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvUserName, tvBorrowDate, tvGiveBackDate, tvBookName, tvIdUser, tvIdBook, tvIdBill,
    tvPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_bill);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin phiếu mượn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarBill);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvUserName = findViewById(R.id.tvUserName);
        tvBorrowDate = findViewById(R.id.tvBorrowDate);
        tvGiveBackDate = findViewById(R.id.tvGiveBackDate);
        tvIdBook = findViewById(R.id.tvIdBook);
        tvBookName = findViewById(R.id.tvBookName);
        tvIdBill = findViewById(R.id.tvIdBill);
        tvPrice = findViewById(R.id.tvPrice);
    }
}