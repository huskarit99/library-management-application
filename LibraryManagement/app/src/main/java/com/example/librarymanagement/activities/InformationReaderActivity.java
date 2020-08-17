package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.Server;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class InformationReaderActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvNameReader, tvIdReader, tvBirthdayReader, tvGenderReader, tvEmailReader, tvAddressReader;
    ImageView imgAvatar;
    User user;
    ImageButton btnDelete;
    public static final String READER = "READER";

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
            user.setImage(user.getImage().replace("p:", "ps:"));
            Picasso.get().load(user.getImage()).into(imgAvatar);
        }else{
            Toast.makeText(InformationReaderActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user== null){
                    Toast.makeText(InformationReaderActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }else{
                    deleteUser();
                }
            }
        });
    }

    private void deleteUser() {
        final String id = String.valueOf(user.getUser_id());
        AlertDialog.Builder builder = new AlertDialog.Builder(InformationReaderActivity.this);
        builder.setMessage("Bạn có muốn xoá độc giả này?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog pd = new ProgressDialog(InformationReaderActivity.this);
                        pd.setMessage("Đang xoá...");
                        pd.show();
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DELETEUSER,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("Success")) {
                                            Toast.makeText(getApplication(), "Xoá độc giả thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplication(), "Xoá độc giả thất bại", Toast.LENGTH_LONG).show();
                                        }
                                        pd.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplication(), "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("userId", id);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit_data){
            Intent intent = new Intent(InformationReaderActivity.this, EditReaderActivity.class);
            intent.putExtra(READER, user);
            startActivity(intent);
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
        btnDelete = findViewById(R.id.btnDelete);
    }
}