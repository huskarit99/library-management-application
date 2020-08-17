package com.example.librarymanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> listUser;

    public UserAdapter(Context context, int layout, List<User> listUser) {
        this.context = context;
        this.layout = layout;
        this.listUser = listUser;
    }
    public void update(ArrayList<User> results){
        listUser = new ArrayList<>();
        listUser.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private static class ViewHolder{
        TextView tvName, tvIdUser, tvBirthday;
        ImageView imgAvatar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        ViewHolder holder = new ViewHolder();
        if(rowView == null){
            rowView = inflater.inflate(layout, null);
            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.tvIdUser = rowView.findViewById(R.id.tvIdUser);
            holder.tvBirthday = rowView.findViewById(R.id.tvBirthday);
            holder.imgAvatar = rowView.findViewById(R.id.imgAvatar);
            rowView.setTag(holder);
        }else{
            holder = (ViewHolder) rowView.getTag();
        }
        holder.tvName.setText(listUser.get(position).getName());
        holder.tvIdUser.setText(String.valueOf(listUser.get(position).getUser_id()));
        holder.tvBirthday.setText(listUser.get(position).getBirthday());
        String path = listUser.get(position).getImage().replace("p:","ps:" );
        Picasso.get().load(path).into(holder.imgAvatar);

        return rowView;
    }
}
