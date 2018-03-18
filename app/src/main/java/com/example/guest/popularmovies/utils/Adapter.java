package com.example.guest.popularmovies.utils;

/**
 * Created by guest on 2/20/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<SingleMovie> movies = new ArrayList<>();
    private Context context; //todo: check
    private LayoutInflater layoutInflater;
    private float dpHeight;
    private float dpWidth;


    public Adapter(LayoutInflater layoutInflater, Context context) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void addMovies(List<SingleMovie> news) {
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
//        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(holder.posterIv);

        holder.title.setText(movie.getTitle());
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .into(holder.poster);
        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.IDENTIFICATION, movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        protected ImageView poster;
        @BindView(R.id.movie_title)
        protected TextView title;
        private final View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setMinimumWidth((int) (dpWidth / 2));
            view.setMinimumHeight((int) ((dpWidth / 2) * 1.5));
            ButterKnife.bind(this, itemView);
        }
    }
}
