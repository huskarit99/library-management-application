package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.models.BorrowedBook;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class BorrowedBookActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ListView listView;
    Toolbar toolbar;

    public int idUser;
    public ArrayList<BorrowedBook> listBorrowedBook;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book);

        mapping();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Danh sách sách mượn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        listBorrowedBook = (ArrayList<BorrowedBook>) intent.getSerializableExtra("BORROWEDBOOK");

        sessionManager = new SessionManager(this);
        idUser = sessionManager.getUser();

        customAdapter = new BorrowedBookActivity.CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    private void mapping() {
        listView = findViewById(R.id.listBorrowedBook);
        toolbar = findViewById(R.id.toolbarBorrowedBook);
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listBorrowedBook.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.row_borrowed_book, null);

            ImageView image = view.findViewById(R.id.avatarBook);
            TextView name = view.findViewById(R.id.nameBook);
            TextView idBook = view.findViewById(R.id.idBook);
            TextView idBill = view.findViewById(R.id.idBill);
            TextView borrowDate = view.findViewById(R.id.borrowDate);
            TextView payDate = view.findViewById(R.id.payDate);

            String path = listBorrowedBook.get(i).getImage();
            path.replace("p:", "ps:");
            Picasso.get().load(path).into(image);
            name.setText(listBorrowedBook.get(i).getName());
            idBook.setText(valueOf(listBorrowedBook.get(i).getBook_id()));
            idBill.setText(valueOf(listBorrowedBook.get(i).getBill_id()));
            borrowDate.setText(listBorrowedBook.get(i).getDateOfBorrowed());
            payDate.setText(listBorrowedBook.get(i).getDateOfPurchase());
            return view;
        }
    }
}

