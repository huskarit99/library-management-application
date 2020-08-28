package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.database.Cursor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.GetData;
import com.example.librarymanagement.networks.Server;
import com.example.librarymanagement.networks.SessionManager;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtName, edtIdUser, edtEmail, edtBirthday, edtAddress;
    private ImageView edtImage, btnCalendar;
    private String user_id, name, role, gender, email, birthday, address, password, state, temp, image;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private DatePickerDialog datePickerDialog;
    private TextView tvId;
    private SessionManager sessionManager;
    private GetData getData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mapping();
        sessionManager = new SessionManager(this);
        getData = new GetData(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm độc giả");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        temp = "Bạn có muốn thêm độc giả này?";
        state = "1";
        role = "1";
        if(sessionManager.getRole()==3){
            Intent intent = getIntent();
            if(intent.getStringExtra("ADD").equals("add_librarian")) {
                getSupportActionBar().setTitle("Thêm thủ thư");
                tvId.setText("Mã thủ thư");
                role = "2";
                temp = "Bạn có muốn thêm thủ thư này?";
                edtIdUser.setHint("Mã thủ thư");
            }
        }

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        gender = radioButton.getText().toString();

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddUserActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year+"-"+month+"-"+day;
                        edtBirthday.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(CheckConnect.isconnected(AddUserActivity.this)){
            if(item.getItemId() == R.id.save_data){
                user_id = edtIdUser.getText().toString();
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                birthday = edtBirthday.getText().toString();
                address = edtAddress.getText().toString();
                password = "123456";
                if(user_id.isEmpty() || name.isEmpty() || birthday.isEmpty() || address.isEmpty() || email.isEmpty() || image==null){
                    Toast.makeText(AddUserActivity.this, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }else{
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddUserActivity.this);
                    builder.setMessage(temp)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog pd = new ProgressDialog(AddUserActivity.this);
                                    pd.setMessage("Đang lưu...");
                                    pd.show();
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.ADDUSER,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if(response.equals("Success")){
                                                        Toast.makeText(AddUserActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                                        if(getSupportActionBar().getTitle().equals("Thêm thủ thư")){
                                                            getListLibrarian();
                                                        }else{
                                                            getListReader();
                                                        }
                                                        finish();
                                                    }else{
                                                        Toast.makeText(AddUserActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                    pd.dismiss();

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(AddUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                }
                                            })
                                    {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("userid", user_id);
                                            params.put("role", role);
                                            params.put("name", name);
                                            params.put("gender", gender);
                                            params.put("email", email);
                                            params.put("birthday", birthday);
                                            params.put("address", address);
                                            params.put("password", password);
                                            params.put("image", image);
                                            params.put("state", state);
                                            return params;
                                        }
                                    };
                                    requestQueue.add(stringRequest);
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                }
            }
        }else{
            Toast.makeText(AddUserActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                edtImage.setImageBitmap(imageBitmap);
                BitMapToString(imageBitmap);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                edtImage.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
        }
    }

    public String BitMapToString(Bitmap userImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return image;
    }

    private void selectImage(){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick (DialogInterface dialog, int item){
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getListReader(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Server.GETALLUSERS +"?role_id="+1;
                RequestQueue requestQueue = Volley.newRequestQueue(AddUserActivity.this);
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
                                Toast.makeText(AddUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(AddUserActivity.this);
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
                                Toast.makeText(AddUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void mapping() {
        radioGroup = findViewById(R.id.radioGroup);
        toolbar = findViewById(R.id.toolbarAddUser);
        edtBirthday = findViewById(R.id.edtBirthday);
        edtName = findViewById(R.id.edtName);
        edtIdUser = findViewById(R.id.edtIdUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtImage = findViewById(R.id.avatarUser);
        btnCalendar = findViewById(R.id.btnCalendar);
        tvId = findViewById(R.id.tvId);
    }
}