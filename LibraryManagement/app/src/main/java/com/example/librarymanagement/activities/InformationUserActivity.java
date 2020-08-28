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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.example.librarymanagement.networks.GetData;
import com.example.librarymanagement.networks.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class InformationUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvNameReader, tvIdReader, tvBirthdayReader, tvGenderReader, tvEmailReader, tvAddressReader,
            tvId;
    ImageView imgAvatar;
    User user;
    ImageButton btnDelete;
    public static final String INFO_USER = "INFO_USER";
    GetData getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData = new GetData(this);
        setContentView(R.layout.activity_information_user);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(UserManagerActivity.INFO_USER);
        if(user!=null){
            tvNameReader.setText(user.getName());
            tvIdReader.setText(String.valueOf(user.getUser_id()));
            if(user.getRole_id()==1){
                getSupportActionBar().setTitle("Thông tin độc giả");
            }else if(user.getRole_id()==2){
                tvId.setText("Mã thủ thư");
                getSupportActionBar().setTitle("Thông tin thủ thư");
            }
            tvBirthdayReader.setText(user.getBirthday());
            tvGenderReader.setText(user.getGender());
            tvEmailReader.setText(user.getEmail());
            tvAddressReader.setText(user.getAddress());
            user.setImage(user.getImage().replace("p:", "ps:"));
            Picasso.get().load(user.getImage()).into(imgAvatar);
        }else{
            Toast.makeText(InformationUserActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user== null){
                    Toast.makeText(InformationUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }else{
                    String role = "Bạn có muốn xoá độc giả này?";
                    if(user.getRole_id()==2){
                        role = "Bạn có muốn xoá thủ thư này?";
                    }
                    deleteUser(role);
                }
            }
        });
    }

    private void deleteUser(String role) {
        final String id = String.valueOf(user.getUser_id());
        AlertDialog.Builder builder = new AlertDialog.Builder(InformationUserActivity.this);
        builder.setMessage(role)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog pd = new ProgressDialog(InformationUserActivity.this);
                        pd.setMessage("Đang xoá...");
                        pd.show();
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DELETEUSER,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("Success")) {
                                            if(user.getRole_id()==1){
                                                Toast.makeText(getApplication(), "Xoá độc giả thành công", Toast.LENGTH_LONG).show();
                                                getListReader();
                                            }else if(user.getRole_id()==2){
                                                Toast.makeText(getApplication(), "Xoá thủ thư thành công", Toast.LENGTH_LONG).show();
                                                getListLibrarian();
                                            }
                                            finish();
                                        } else {
                                            Toast.makeText(getApplication(), "Xoá thất bại", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(InformationUserActivity.this, EditUserActivity.class);
            intent.putExtra(INFO_USER, user);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListReader(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+1;
                RequestQueue requestQueue = Volley.newRequestQueue(InformationUserActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListReader(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(InformationUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void getListLibrarian(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+2;
                RequestQueue requestQueue = Volley.newRequestQueue(InformationUserActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListLibrarian(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(InformationUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarInfoReader);
        tvNameReader = findViewById(R.id.tvNameReader);
        tvIdReader = findViewById(R.id.tvIdReader);
        tvBirthdayReader = findViewById(R.id.tvBirthdayReader);
        tvGenderReader = findViewById(R.id.tvGenderReader);
        tvEmailReader = findViewById(R.id.tvEmailReader);
        tvAddressReader = findViewById(R.id.tvAddressReader);
        tvId = findViewById(R.id.tvId);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnDelete = findViewById(R.id.btnDelete);
    }
}