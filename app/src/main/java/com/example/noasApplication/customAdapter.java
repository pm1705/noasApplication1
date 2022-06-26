package com.example.noasApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> nameList;
    String[] images;
    LayoutInflater inflter;

    public customAdapter(Context applicationContext, ArrayList<String> nameList, String[] images) {
        this.context = context;
        this.nameList = nameList;
        this.images = images;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_lv_layout, null);
        TextView name = (TextView) view.findViewById(R.id.text_View);
        ImageView image = (ImageView) view.findViewById(R.id.image_view);
        name.setText(nameList.get(i));
        try {
            Picasso.get().load(images[i]).into(image);
        }
        catch(Exception e){
            System.out.println("error");
        }
        return view;
    }
}
