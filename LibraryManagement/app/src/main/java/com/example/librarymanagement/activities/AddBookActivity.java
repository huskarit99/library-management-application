package com.example.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.librarymanagement.networks.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView edtBookName, edtPublishingYear, edtAuthor, edtCategory, edtPublisher;
    private ImageView edtImage;

    String[] authors_name, categories_name, publishers_name;
    Integer[] authors_id, categories_id, publishers_id;
    private String image, book_name, publishing_year, publisher, category;

    JSONArray selectedAuthors;

    ArrayList<Integer> mAuthor = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm sách");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        getAuthors();
        edtAuthor.setInputType(InputType.TYPE_NULL);
        edtAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAuthors();
            }
        }) ;

        getCategory();
        edtCategory.setInputType(InputType.TYPE_NULL);
        edtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory();
            }
        });

        getPublisher();
        edtPublisher.setInputType(InputType.TYPE_NULL);
        edtPublisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPublisher();
            }
        });
    }

    void init(){
        image = "";
        book_name = "";
        publishing_year = "";
        publisher = "";
        category = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_data) {
            book_name = edtBookName.getText().toString();
            publishing_year = edtPublishingYear.getText().toString();
            final String amount = "1";
            if (book_name == "" || publishing_year == "" || image == "" || publisher == "" || category == "" || selectedAuthors.length() == 0) {
                Toast.makeText(AddBookActivity.this, "Bạn đã cung cấp thiếu thông tin !!!", Toast.LENGTH_SHORT).show();
            }
            else{
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.ADDBOOK,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(AddBookActivity.this, "Cập nhật dữ liệu thành công", Toast.LENGTH_SHORT).show();
//                                finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(AddBookActivity.this, "Cập nhật dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("bookname", book_name);
                            params.put("publicationyear", publishing_year);
                            params.put("image", image);
                            params.put("publisher", publisher);
                            params.put("category", category);
                            params.put("authors", selectedAuthors.toString());
                            params.put("amount", amount);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        return super.onOptionsItemSelected(item);
    }

    public void getAuthors(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLAUTHORS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        authors_name = new String[response.length()];
                        authors_id = new Integer[response.length()];
                         for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                authors_name[i] = jsonObject.getString("name");
                                authors_id[i] = Integer.parseInt(jsonObject.getString("author_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddBookActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    public void selectAuthors(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setTitle("List Authors");
        boolean[] checkedAuthor;
        final Map<String, Integer> mapAuthors = new HashMap<String, Integer>();
        for (int i = 0; i < authors_name.length; i++){
            mapAuthors.put(authors_name[i], authors_id[i]);
        }
        Arrays.sort(authors_name);
        checkedAuthor = new boolean[authors_name.length];
        mAuthor.clear();
        builder.setMultiChoiceItems(authors_name, checkedAuthor, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean ischecked) {
                if (ischecked){
                    if (!mAuthor.contains(position)){
                        mAuthor.add(position);
                    }else{
                        mAuthor.remove(position);
                    }
                }
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                String text = "";
                JSONArray mapping_id = new JSONArray();
                for (int i = 0; i < mAuthor.size(); i++){
                    if (i != 0)
                        text += ", ";
                    String name = authors_name[mAuthor.get(i)];
                    Integer id = mapAuthors.get(name);
                    text += name;
                    mapping_id.put(id);
                }
                edtAuthor.setText(text);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ID", mapping_id);
                    selectedAuthors = new JSONArray();
                    selectedAuthors.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public void BitMapToString(Bitmap bookImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bookImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void getCategory(){
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLCATEGORIES, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        categories_name = new String[response.length()];
                        categories_id = new Integer[response.length()];
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                categories_name[i] = jsonObject.getString("name");
                                categories_id[i] = Integer.parseInt(jsonObject.getString("category_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddBookActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    public void selectCategory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setTitle("List Categories");
        final Map<String, Integer> mapCategories = new HashMap<String, Integer>();
        for (int i = 0; i < categories_name.length; i++)
            mapCategories.put(categories_name[i], categories_id[i]);
        Arrays.sort(categories_name);
        category = mapCategories.get(categories_name[0]).toString();
        builder.setSingleChoiceItems(categories_name, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                category = mapCategories.get(categories_name[position]).toString();
                edtCategory.setText(categories_name[position]);
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog Dialog = builder.create();
        Dialog.show();
    }

    public void getPublisher(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        JsonArrayRequest arrayReq = new JsonArrayRequest(Request.Method.GET, Server.GETALLPUBLISHERS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        publishers_name = new String[response.length()];
                        publishers_id = new Integer[response.length()];
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                publishers_name[i] = jsonObject.getString("name");
                                publishers_id[i] = Integer.parseInt(jsonObject.getString("publisher_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddBookActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(arrayReq);
    }

    public void selectPublisher(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setTitle("List Categories");
        final Map<String, Integer> mapPublishers = new HashMap<String, Integer>();
        for (int i = 0; i < publishers_name.length; i++){
            mapPublishers.put(publishers_name[i], publishers_id[i]);
        }
        Arrays.sort(publishers_name);
        publisher = mapPublishers.get(publishers_name[0]).toString();
        builder.setSingleChoiceItems(publishers_name, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                publisher = mapPublishers.get(publishers_name[position]).toString();
                edtPublisher.setText(publishers_name[position]);
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog Dialog = builder.create();
        Dialog.show();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarAddBook);
        edtBookName = findViewById(R.id.edtBookName);
        edtPublishingYear = findViewById(R.id.edtPublishingYear);
        edtImage = findViewById(R.id.avatarBook);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtCategory = findViewById(R.id.edtCategory);
        edtPublisher = findViewById(R.id.edtPublisher);
    }
}