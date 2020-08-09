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

import com.example.librarymanagement.R;
import com.example.librarymanagement.networks.SessionManager;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbarHome;
    LinearLayout linearLogout;
    SessionManager sessionManager;
    CardView cardViewLibrarian, cardViewAdmin;
    static boolean count;

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
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();

            }
        });


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

    private void mapping() {
        toolbarHome = findViewById(R.id.toolbarHome);
        linearLogout = findViewById(R.id.lnLogout);
        cardViewLibrarian = findViewById(R.id.cardViewLibrarian);
        cardViewAdmin = findViewById(R.id.cardViewAdmin);
    }
}