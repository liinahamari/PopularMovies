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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private List<SingleMovie> movies = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public Adapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void addNews(List<SingleMovie> news) {
        movies.addAll(news);
        notifyDataSetChanged();
    }

    public void clearNews() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SingleMovie movie = movies.get(position);
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster) ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }
}
