package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Book;
import com.example.librarymanagement.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class SearchBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchBar;

    private Button btnNameBook, btnAuthor, btnCategory, btnIdBook;


    private String keyword = "";
    private int btnNumber = 0;
    ArrayList<Book> listBook, listBookLookUp;

    private CustomAdapterBook customAdapterBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        Intent intent = getIntent();
        listBook =  (ArrayList<Book>)intent.getSerializableExtra(HomeActivity.BOOK);
        listBookLookUp = new ArrayList<Book>(listBook);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tra cứu sách");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        turnOnButton(btnNameBook);
        updateListView();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(""))
                    keyword="";
                else
                    keyword = new NormalizeString(s).getNormalizeString();
                searchNewText();
                return false;
            }
        });

        btnNameBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNumber = 0;
                turnOnButton(btnNameBook);
                turnOffButton(btnAuthor);
                turnOffButton(btnCategory);
                turnOffButton(btnIdBook);
                searchByNameBook();
            }
        });

        btnAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNumber = 1;
                System.out.println(keyword);
                turnOffButton(btnNameBook);
                turnOnButton(btnAuthor);
                turnOffButton(btnCategory);
                turnOffButton(btnIdBook);
                searchByAuthor();
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNumber = 2;
                System.out.println(keyword);
                turnOffButton(btnNameBook);
                turnOffButton(btnAuthor);
                turnOnButton(btnCategory);
                turnOffButton(btnIdBook);
                searchByCategory();
            }
        });

        btnIdBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNumber = 3;
                System.out.println(keyword);
                turnOffButton(btnNameBook);
                turnOffButton(btnAuthor);
                turnOffButton(btnCategory);
                turnOnButton(btnIdBook);
                searchByIdBook();
            }
        });
    }

    private void searchNewText() {
        switch(btnNumber) {
            case 0:
                searchByNameBook();
                break;
            case 1:
                searchByAuthor();
                break;
            case 2:
                searchByCategory();
                break;
            case 3:
                searchByIdBook();
                break;
        }
    }

    private void updateListView(){
        customAdapterBook = new CustomAdapterBook();
        listView.setAdapter(customAdapterBook);
    }

    private void searchByNameBook() {
        listBookLookUp = new ArrayList<>();
        for (int i = 0; i < listBook.size(); i++){
            String normalizeString = new NormalizeString(listBook.get(i).getName()).getNormalizeString();
            if (normalizeString.contains(keyword)){
                listBookLookUp.add(listBook.get(i));
            }
        }
        updateListView();
    }
    private void searchByAuthor() {
        listBookLookUp = new ArrayList<>();
        for (int i = 0; i < listBook.size(); i++){
            String normalizeString = new NormalizeString(listBook.get(i).getAuthors()).getNormalizeString();
            if (normalizeString.contains(keyword)){
                listBookLookUp.add(listBook.get(i));
            }
        }
        updateListView();
    }
    private void searchByIdBook() {
        listBookLookUp = new ArrayList<>();
        for (int i = 0; i < listBook.size(); i++){
            if (valueOf(listBook.get(i).getBook_id()).contains(keyword)){
                listBookLookUp.add(listBook.get(i));
            }
        }
        updateListView();
    }
    private void searchByCategory() {
        listBookLookUp = new ArrayList<>();
        for (int i = 0; i < listBook.size(); i++){
            String normalizeString = new NormalizeString(listBook.get(i).getCategory()).getNormalizeString();
            if (normalizeString.contains(keyword)){
                listBookLookUp.add(listBook.get(i));
            }
        }
        updateListView();
    }

    private void turnOnButton(Button btn){
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_search_clicked));
        btn.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void turnOffButton(Button btn){
        btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_search));
        btn.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    class CustomAdapterBook extends BaseAdapter{

        @Override
        public int getCount() {
            return listBookLookUp.size();
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
            view = getLayoutInflater().inflate(R.layout.row_book, null);

            ImageView image = view.findViewById(R.id.imageBook);
            TextView id = view.findViewById(R.id.idBook);
            TextView name = view.findViewById(R.id.nameBook);
            TextView author = view.findViewById(R.id.author);
            TextView category = view.findViewById(R.id.category);
            TextView amount = view.findViewById(R.id.amount);

            String path = listBookLookUp.get(i).getImage();
            path.replace("p:", "ps:");
            Picasso.get().load(path).into(image);
            id.setText(valueOf(listBookLookUp.get(i).getBook_id()));
            name.setText(listBookLookUp.get(i).getName());
            author.setText(listBookLookUp.get(i).getAuthors());
            category.setText(listBookLookUp.get(i).getCategory());
            if (listBookLookUp.get(i).getAmount() == 0)
                amount.setText("Tạm thời không còn");
            else
                amount.setText("Có sẵn");
            return view;
        }
    }


    private void mapping() {
        toolbar = findViewById(R.id.toolbarSearch);
        listView = findViewById(R.id.listBook);
        btnAuthor = findViewById(R.id.btnAuthor);
        btnCategory = findViewById(R.id.btnCategory);
        btnIdBook = findViewById(R.id.btnIdBook);
        btnNameBook = findViewById(R.id.btnNameBook);
        searchBar = findViewById(R.id.searchBar);
    }
}