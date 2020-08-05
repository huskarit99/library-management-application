package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagement.R;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.Server;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.content.Intent;
import android.net.Uri;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;


public class AddUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtName, edtIdUser, edtEmail, edtBirthday, edtAddress;
    private ImageView edtImage, btnCalendar;
    private String user_id, name, role, gender, email, birthday, address, password;
    private String image;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm độc giả");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(CheckConnect.isconnected(AddUserActivity.this)){
            if(item.getItemId() == R.id.save_data){
                user_id = edtIdUser.getText().toString();
                role ="1";
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                birthday = edtBirthday.getText().toString();
                address = edtAddress.getText().toString();
                password = "123456";
                System.out.println(image.length());
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addUser,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(AddUserActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AddUserActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
                            return params;
                    }
                };
                requestQueue.add(stringRequest);
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
        System.out.println(this);
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
    }
}