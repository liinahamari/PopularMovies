package com.example.guest.popularmovies.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.data.model.SingleMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by guest on 2/20/18.
 */

public class ArrayAdapter extends android.widget.ArrayAdapter<SingleMovie> {
    private static final String URL_BASE = "http://image.tmdb.org/t/p/w185";
    Context context;

    public ArrayAdapter(List<SingleMovie> items, Context context) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            AppCompatActivity activity = (AppCompatActivity) context;
            convertView = activity.getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
        }

        SingleMovie movie = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.poster);
        String s = URL_BASE + movie.getPosterPath();
        Picasso.with(context).load(s).into(imageView);
        return convertView;
    }
}
