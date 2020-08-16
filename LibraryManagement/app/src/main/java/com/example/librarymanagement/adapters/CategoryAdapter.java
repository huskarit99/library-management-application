package com.example.librarymanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Category> listCategory;

    public CategoryAdapter(Context context, int layout, List<Category> listCategory) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private static class ViewHolder{
        TextView tvNameCategory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        ViewHolder holder = new CategoryAdapter.ViewHolder();
        if(rowView==null){
            rowView = inflater.inflate(layout, null);
            holder.tvNameCategory = rowView.findViewById(R.id.tvNameCategory);
            rowView.setTag(holder);
        }else{
            holder = (ViewHolder) rowView.getTag();
        }
        holder.tvNameCategory.setText(listCategory.get(position).getName());
        return rowView;
    }
}
