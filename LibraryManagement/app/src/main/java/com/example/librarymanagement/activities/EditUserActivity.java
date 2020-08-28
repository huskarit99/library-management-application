package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView  tvIdReader, tvId;
    EditText edtNameReader, edtBirthDayReader, edtEmailReader, edtAddressReader;
    ImageView imgAvatar, btnCalendar;
    User user;
    RadioGroup radioGroup;
    RadioButton radioMan, radioWoman, radioButton;
    DatePickerDialog datePickerDialog;
    GetData getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        getData = new GetData(this);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chỉnh sửa thông tin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(InformationUserActivity.INFO_USER);
        if (user != null) {
            edtNameReader.setText(user.getName());
            tvIdReader.setText(String.valueOf(user.getUser_id()));
            if(user.getRole_id()==2){
                tvId.setText("Mã thủ thư");
            }
            edtBirthDayReader.setText(user.getBirthday());
            edtEmailReader.setText(user.getEmail());
            edtAddressReader.setText(user.getAddress());
            Picasso.get().load(user.getImage()).into(imgAvatar);
            if (radioMan.getText().equals(user.getGender())) {
                radioMan.setChecked(true);
            } else {
                radioWoman.setChecked(true);
            }
        } else {
            Toast.makeText(EditUserActivity.this, "Load dữ liệu không thành công", Toast.LENGTH_SHORT).show();
        }
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(EditUserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + day;
                        edtBirthDayReader.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_data:
                edtNameReader.setText(user.getName());
                edtBirthDayReader.setText(user.getBirthday());
                edtEmailReader.setText(user.getEmail());
                edtAddressReader.setText(user.getAddress());
                if (radioMan.getText().equals(user.getGender())) {
                    radioMan.setChecked(true);
                } else {
                    radioWoman.setChecked(true);
                }
                break;
            case R.id.save_data:
                String role = "Bạn có muốn chỉnh sửa độc giả này?";
                if(user.getRole_id()==2){
                    role = "Bạn có muốn chỉnh sửa thủ thư này?";
                }
                final int userId = user.getUser_id();
                final String name = edtNameReader.getText().toString();
                final String birthDay = edtBirthDayReader.getText().toString().trim();
                final String email = edtEmailReader.getText().toString().trim();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                final String gender = radioButton.getText().toString();
                final String address = edtAddressReader.getText().toString().trim();
                if(name.equals(user.getName()) && birthDay.equals(user.getBirthday()) && email.equals(user.getEmail()) && gender.equals(user.getGender())
                && address.equals(user.getAddress())){
                    Toast.makeText(EditUserActivity.this, "Bạn chưa nhập thông tin mới", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivity.this);
                    builder.setMessage(role)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ProgressDialog pd = new ProgressDialog(EditUserActivity.this);
                                    pd.setMessage("Đang lưu...");
                                    pd.show();
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.EDITUSER,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            if (response.equals("Success")) {
                                                                if(user.getRole_id()==1){
                                                                    Toast.makeText(EditUserActivity.this, "Sửa thông tin độc giả thành công", Toast.LENGTH_SHORT).show();
                                                                    getListReader();
                                                                }else if(user.getRole_id()==2){
                                                                    Toast.makeText(EditUserActivity.this, "Sửa thông tin thủ thư thành công", Toast.LENGTH_SHORT).show();
                                                                    getListLibrarian();
                                                                }
                                                                finish();

                                                            } else {
                                                                Toast.makeText(EditUserActivity.this, "Lỗi truy vấn", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(EditUserActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("userId",String.valueOf(userId));
                                                    params.put("name", name);
                                                    params.put("gender", gender);
                                                    params.put("email", email);
                                                    params.put("birthday", birthDay);
                                                    params.put("address", address);
                                                    return params;
                                                }
                                            };
                                            requestQueue.add(stringRequest);
                                        }
                                    });
                                    thread.start();
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void getListReader(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+1;
                RequestQueue requestQueue = Volley.newRequestQueue(EditUserActivity.this);
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
                                Toast.makeText(EditUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(EditUserActivity.this);
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
                                Toast.makeText(EditUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarEditReader);
        edtNameReader = findViewById(R.id.edtNameReader);
        tvIdReader = findViewById(R.id.tvIdReader);
        tvId = findViewById(R.id.tvId);
        edtBirthDayReader = findViewById(R.id.edtBirthdayReader);
        edtEmailReader = findViewById(R.id.edtEmailReader);
        edtAddressReader = findViewById(R.id.edtAddressReader);
        imgAvatar = findViewById(R.id.avatarReader);
        btnCalendar = findViewById(R.id.btnCalendar);
        radioGroup = findViewById(R.id.radioGroup);
        radioMan = findViewById(R.id.radioMan);
        radioWoman = findViewById(R.id.radioWoman);

    }
}