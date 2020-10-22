package com.bhaskar.doorstep.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.SpinnerDTO;

import java.util.ArrayList;
import java.util.List;

public class SpinnerViewAdapter extends ArrayAdapter<SpinnerDTO> {
    Context context;
    LayoutInflater inflater;
    ArrayList<SpinnerDTO> spinnerList;
    ViewHolder holder = null;
    private static final String TAG = "SpinnerViewAdapter";

    public SpinnerViewAdapter(Context context, int textViewResourceId, ArrayList<SpinnerDTO> spinnerList) {
        super(context, textViewResourceId, spinnerList);
        inflater = ((Activity) context).getLayoutInflater();
        this.spinnerList = spinnerList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        SpinnerDTO listItemAddProg = spinnerList.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.spinner_row, parent, false);
            holder.name = (TextView) row.findViewById(R.id.spinner_name);
            holder.img = (ImageView) row.findViewById(R.id.spinner_img);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Log.d(TAG, "getCustomView: name= "+listItemAddProg.getName());
        Log.d(TAG, "getCustomView: image= "+listItemAddProg.getImage());
        holder.name.setText(listItemAddProg.getName());
        holder.img.setBackgroundResource(listItemAddProg.getImage());

        return row;
    }



    static class ViewHolder{
        TextView name;
        ImageView img;

    }
}
