package com.example.test.ppobo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import users.User;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CareGiverAdapter extends ArrayAdapter<User> {

    private Context mContext;
    int mResource;

    public CareGiverAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get information to be put in list adapter
        String name = getItem(position).getName();
        String email = getItem(position).getEmail();
        String phoneNum = getItem(position).getPhoneNum();

        // Adjust fragment to fit into List View
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false );

        // Initialize Text Views
        TextView tvName = convertView.findViewById(R.id.adapter_name);
        TextView tvEmail = convertView.findViewById(R.id.adapter_email);
        TextView tvPhoneNum = convertView.findViewById(R.id.adapter_phone_num);

        // Set TextViews
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhoneNum.setText(phoneNum);

        return convertView;
    }
}
