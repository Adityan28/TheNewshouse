package com.example.android.thenewshouse;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 11-07-2017.
 */

public class NewsItemAdapter extends ArrayAdapter<NewsItems>{

    NewsItemAdapter(Activity context, List<NewsItems> arraylist){
        super(context,0,arraylist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsItems newsItems = getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.activity_main,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image_view);
        imageView.setImageBitmap(newsItems.getmImageBitmap());

        TextView textView1 = (TextView)convertView.findViewById(R.id.head_text_view);
        textView1.setText(newsItems.getmNewsHead());

        TextView textView2 = (TextView)convertView.findViewById(R.id.time_text_view);
        textView2.setText(newsItems.getmDateofPublication());

        TextView textView3 = (TextView)convertView.findViewById(R.id.section_text_view);
        textView3.setText(newsItems.getmSection());

        return convertView;
    }
}
