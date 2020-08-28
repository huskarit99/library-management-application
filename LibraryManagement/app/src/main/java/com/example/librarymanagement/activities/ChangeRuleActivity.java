package com.example.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.librarymanagement.adapters.CategoryAdapter;
import com.example.librarymanagement.models.Category;
import com.example.librarymanagement.networks.CheckConnect;
import com.example.librarymanagement.networks.GetData;
import com.example.librarymanagement.networks.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeRuleActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtMaxDay, edtMaxBook, edtFine;
    ImageButton btnSaveRule, btnAddCategory;
    ListView listCategories;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    String url, id, price, maxBook, maxDay;
    GetData getData;
    JSONArray responseRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rule);
        getData = new GetData(this);
        mapping();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thay đổi quy định");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        categoryArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, R.layout.row_category, categoryArrayList);
        if (CheckConnect.isconnected(ChangeRuleActivity.this)) {
            if(getData.getRule().equals("")){
                Toast.makeText(ChangeRuleActivity.this, "Không thể load quy định", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    responseRule = new JSONArray(getData.getRule());
                    JSONObject jsonObject = responseRule.getJSONObject(0);
                    id = jsonObject.getString("id");
                    price = jsonObject.getString("price");
                    maxBook = jsonObject.getString("maximum_borrowed_books");
                    maxDay = jsonObject.getString("maximum_borrowed_days");
                    edtFine.setText(price);
                    edtMaxBook.setText(maxBook);
                    edtMaxDay.setText(maxDay);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(getData.getListCategory().equals("")){
                Toast.makeText(ChangeRuleActivity.this, "Không thể load thể loại", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONArray responseCategory = new JSONArray(getData.getListCategory());
                    for (int i = 0; i < responseCategory.length(); i++) {
                        JSONObject jsonObjectCategory = responseCategory.getJSONObject(i);
                        categoryArrayList.add(new Category(
                                jsonObjectCategory.getInt("category_id"),
                                jsonObjectCategory.getString("name")
                        ));
                        categoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listCategories.setAdapter(categoryAdapter);
        } else {
            Toast.makeText(ChangeRuleActivity.this, "Thiết bị chưa kết nối mạng", Toast.LENGTH_SHORT).show();
        }
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddCategory();
            }
        });
        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogEditCategory(categoryArrayList.get(position));
            }
        });
        btnSaveRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRule();
            }
        });

    }

    private boolean isNumberic(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void EditRule() {
        final String tempPrice = edtFine.getText().toString();
        final String tempMaxBook = edtMaxBook.getText().toString();
        final String tempMaxDay = edtMaxDay.getText().toString();
        if (tempPrice.isEmpty() || tempMaxBook.isEmpty() || tempMaxDay.isEmpty()) {
            Toast.makeText(ChangeRuleActivity.this, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            if (tempPrice.equals(price) && tempMaxBook.equals(maxBook) && tempMaxDay.equals(maxDay)) {
                Toast.makeText(ChangeRuleActivity.this, "Bạn chưa nhập dữ liệu mới", Toast.LENGTH_SHORT).show();
            } else {
                if (!isNumberic(tempPrice) || !isNumberic(tempMaxBook) || !isNumberic(tempMaxDay)) {
                    Toast.makeText(ChangeRuleActivity.this, "Dữ liệu phải là số", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(ChangeRuleActivity.this);
                    pd.setMessage("Đang lưu...");
                    pd.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.EDITRULE,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("Success")) {
                                                Toast.makeText(ChangeRuleActivity.this, "Chỉnh sửa quy định thành công", Toast.LENGTH_SHORT).show();
                                                edtFine.setText(tempPrice);
                                                edtMaxBook.setText(tempMaxBook);
                                                edtMaxDay.setText(tempMaxDay);
                                                try {
                                                    JSONObject jsonObject = responseRule.getJSONObject(0);
                                                    jsonObject.put("price",tempPrice);
                                                    jsonObject.put("maximum_borrowed_books",tempMaxBook);
                                                    jsonObject.put("maximum_borrowed_days", tempMaxDay);
                                                    responseRule.put(0, jsonObject);
                                                    getData.setRule(responseRule);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(ChangeRuleActivity.this, "Chỉnh sửa quy định thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                            pd.dismiss();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ChangeRuleActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", id);
                                    params.put("price", tempPrice);
                                    params.put("maximum_borrowed_books", tempMaxBook);
                                    params.put("maximum_borrowed_days", tempMaxDay);
                                    return params;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }
                    });
                    thread.start();

                }

            }
        }
    }

    private void DialogEditCategory(final Category c) {
        final Dialog dialog = new Dialog(ChangeRuleActivity.this);
        dialog.setContentView(R.layout.dialog_edit_category);
        dialog.setCanceledOnTouchOutside(false);
        final EditText edtAddNameCategory = dialog.findViewById(R.id.edtNameCategory);
        edtAddNameCategory.setText(c.getName());
        ImageButton btnAdd = dialog.findViewById(R.id.btnSave);
        ImageButton btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edtAddNameCategory.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(ChangeRuleActivity.this, "Bạn chưa nhập thể loại", Toast.LENGTH_SHORT).show();
                } else {
                    int temp = 0;
                    for (Category category : categoryArrayList) {
                        if (category.getName().equals(name)) {
                            Toast.makeText(ChangeRuleActivity.this, "Thể loại đã có sẵn", Toast.LENGTH_SHORT).show();
                            temp = 1;
                        }
                    }
                    if (temp == 0) {
                        final ProgressDialog pd = new ProgressDialog(ChangeRuleActivity.this);
                        pd.setMessage("Đang lưu...");
                        pd.show();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.EDITCATEGORY,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("Success")) {
                                                    Toast.makeText(ChangeRuleActivity.this, "Chỉnh sửa thể loại thành công", Toast.LENGTH_SHORT).show();
                                                    for (int i = 0; i < categoryArrayList.size(); i++) {
                                                        if (categoryArrayList.get(i).getCategory_id() == c.getCategory_id()) {
                                                            categoryArrayList.get(i).setName(name);
                                                            categoryAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                    getListCategory();
                                                } else {
                                                    Toast.makeText(ChangeRuleActivity.this, "Chỉnh sửa thể loại thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                                pd.dismiss();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ChangeRuleActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("category_id", String.valueOf(c.getCategory_id()));
                                        params.put("name", name);
                                        return params;
                                    }
                                };
                                requestQueue.add(stringRequest);
                            }
                        });
                        thread.start();

                        dialog.dismiss();
                    }

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void DialogAddCategory() {
        final Dialog dialog = new Dialog(ChangeRuleActivity.this);
        dialog.setContentView(R.layout.dialog_add_category);
        dialog.setCanceledOnTouchOutside(false);
        final EditText edtAddNameCategory = dialog.findViewById(R.id.edtNameCategory);
        ImageButton btnAdd = dialog.findViewById(R.id.btnSave);
        ImageButton btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edtAddNameCategory.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(ChangeRuleActivity.this, "Bạn chưa nhập thể loại", Toast.LENGTH_SHORT).show();
                } else {
                    int temp = 0;
                    for (Category category : categoryArrayList) {
                        if (category.getName().equals(name)) {
                            Toast.makeText(ChangeRuleActivity.this, "Thể loại đã có sẵn", Toast.LENGTH_SHORT).show();
                            temp = 1;
                        }
                    }
                    if (temp == 0) {
                        final ProgressDialog pd = new ProgressDialog(ChangeRuleActivity.this);
                        pd.setMessage("Đang lưu...");
                        pd.show();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.ADDCATEGORY,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("Success")) {
                                                    Toast.makeText(ChangeRuleActivity.this, "Thêm thể loại thành công", Toast.LENGTH_SHORT).show();
                                                    int length = categoryArrayList.size();
                                                    Category category = new Category(length + 1, name);
                                                    categoryArrayList.add(category);
                                                    categoryAdapter.notifyDataSetChanged();
                                                    getListCategory();
                                                } else {
                                                    Toast.makeText(ChangeRuleActivity.this, "Thêm thể loại thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                                pd.dismiss();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ChangeRuleActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("name", name);
                                        return params;
                                    }
                                };
                                requestQueue.add(stringRequest);
                            }
                        });
                        thread.start();
                        dialog.dismiss();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getListCategory(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ChangeRuleActivity.this);
                JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, Server.GETALLCATEGORIES, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(!response.toString().equals("empty")){
                                    getData.setListCategory(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ChangeRuleActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread .start();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbarChangeRule);
        edtMaxDay = findViewById(R.id.edtMaxDay);
        edtMaxBook = findViewById(R.id.edtMaxBook);
        edtFine = findViewById(R.id.edtFine);
        btnSaveRule = findViewById(R.id.btnSaveRule);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        listCategories = findViewById(R.id.listCategories);
    }
}