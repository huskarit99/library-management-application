package com.example.librarymanagement.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Author;
import com.example.librarymanagement.models.BookOfAuthors;
import com.example.librarymanagement.models.BorrowedBook;
import com.example.librarymanagement.models.Category;
import com.example.librarymanagement.models.Publisher;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbarHome;
    LinearLayout linearLogout, linearInfo, linearReaderManager, linearLibrarianManager, linearRule, linearSearchBook, linearBookManager,
            linearBill, linearBorrowedBook;

    SessionManager sessionManager;
    CardView cardViewLibrarian, cardViewAdmin;
    ImageButton searchBook, bookManager;

    static boolean count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        sessionManager = new SessionManager(this);

        setSupportActionBar(toolbarHome);
        getSupportActionBar().setTitle("Trang chủ");

        if (sessionManager.getRole() == 1) {
            cardViewLibrarian.setVisibility(View.GONE);
            cardViewAdmin.setVisibility(View.GONE);
        } else {
            if (sessionManager.getRole() == 2) {
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
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                sessionManager.clear();
                                startActivity(intent);
                                onDestroy();
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        linearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, InformationUserActivity.class);
                startActivity(intent);
            }
        });

        linearReaderManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReaderManagerActivity.class);
                startActivity(intent);
            }
        });
        linearLibrarianManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LibrarianManagerActivity.class);
                startActivity(intent);
            }
        });

        linearRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChangeRuleActivity.class);
                startActivity(intent);
            }
        });

        linearSearchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra("BEFORE", "SEARCH");
                startActivity(intent);
            }
        });

        linearBookManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchBookActivity.class);
                intent.putExtra("BEFORE", "MANAGEMENT");
                startActivity(intent);
            }
        });

        linearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BorrowManagerActivity.class);
                startActivity(intent);
            }
        });

        linearBorrowedBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BorrowedBookActivity.class);
                startActivity(intent);
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
        if (!count) {
            count = true;
            Toast.makeText(HomeActivity.this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    private void mapping() {
        toolbarHome = findViewById(R.id.toolbarHome);
        linearLogout = findViewById(R.id.lnLogout);
        linearInfo = findViewById(R.id.lnInfo);
        searchBook = findViewById(R.id.searchBook);
        bookManager = findViewById(R.id.bookManager);
        linearReaderManager = findViewById(R.id.lnReaderManager);
        linearLibrarianManager = findViewById(R.id.lnLibrarianManager);
        linearRule = findViewById(R.id.lnRule);
        linearSearchBook = findViewById(R.id.lnSearchBook);
        linearBookManager = findViewById(R.id.lnBookManager);
        linearBill = findViewById(R.id.lnBill);
        linearBorrowedBook =findViewById(R.id.lnBorrowedBook);
        cardViewLibrarian = findViewById(R.id.cardViewLibrarian);
        cardViewAdmin = findViewById(R.id.cardViewAdmin);
    }
}