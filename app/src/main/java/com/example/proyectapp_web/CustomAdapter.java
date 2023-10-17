package com.example.proyectapp_web;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<User> userList;

    public CustomAdapter(Context context, int layout, ArrayList<User> userList) {
        this.context = context;
        this.layout = layout;
        this.userList = userList;
    }
    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }
    public void add(User user) {
        userList.add(user);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }
        TextView txtId = convertView.findViewById(R.id.txtId);
        TextView txtNombre = convertView.findViewById(R.id.txtNombre);
        TextView txtEmail = convertView.findViewById(R.id.txtEmail);
        TextView txtRol = convertView.findViewById(R.id.txtRol);
        TextView txtImg = convertView.findViewById(R.id.txtImg);
        User user = userList.get(position);
        txtId.setText(user.getId());
        txtNombre.setText(user.getNombre());
        txtEmail.setText(user.getEmail());
        txtRol.setText(user.getRol());
        txtImg.setText(user.getImg());
        return convertView;
    }
}
