package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Book;
import com.squareup.picasso.Picasso;

import static java.lang.String.valueOf;

import java.util.ArrayList;

public class InformationBookActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView avatarBook;
    TextView nameBook, status, category, idBook, authors, publicationYear, publisher;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_book);

        Intent intent = getIntent();
        book =  (Book)intent.getSerializableExtra("INFOBOOK");
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin chi tiết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String path = book.getImage();
        path.replace("p:", "ps:");
        Picasso.get().load(path).into(avatarBook);
        nameBook.setText(book.getName());
        idBook.setText(valueOf(book.getBook_id()));
        category.setText(book.getCategory());
        authors.setText(book.getAuthors());
        publisher.setText(book.getPublisher());
        publicationYear.setText(valueOf(book.getPublicationYear()));
        if (book.getAmount() == 0){
            status.setText("Tạm thời không còn");
            status.setTextColor(getResources().getColor(R.color.notavailable));
        } else{
            status.setText("Có sẵn");
            status.setTextColor(getResources().getColor(R.color.available));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InformationBookActivity.this, SearchBookActivity.class);
        startActivity(intent);
        finish();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarInfoBook);
        avatarBook = findViewById(R.id.avatarBook);
        nameBook = findViewById(R.id.nameBook);
        idBook = findViewById(R.id.idBook);
        category = findViewById(R.id.Category);
        status = findViewById(R.id.status);
        authors = findViewById(R.id.authors);
        publicationYear = findViewById(R.id.publicationYear);
        publisher = findViewById(R.id.publisher);
    }
}