package com.example.guest.popularmovies.utils;

/**
 * Created by guest on 2/20/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.data.model.SingleMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by l1maginaire on 1/25/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private List<SingleMovie> movieList;
    private Context context;

    public Adapter(List<SingleMovie> movieList, Context mContext) {
        this.movieList = movieList;
        this.context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclers_single_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(movieList.get(position).getPosterPath()).resize(75, 75).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        if (movieList == null)
            return 0;
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}