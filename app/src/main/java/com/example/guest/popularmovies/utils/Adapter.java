package com.example.guest.popularmovies.utils;

/**
 * Created by l1maginaire on 2/20/18.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.ui.DetailActivity;
import com.example.guest.popularmovies.ui.MainActivity;
import com.example.guest.popularmovies.utils.pagination.MakeContentValues;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<SingleMovie> movies;
    private Context context;
    private LayoutInflater layoutInflater;
    private float dpHeight;
    private float dpWidth;


    public Adapter(LayoutInflater layoutInflater, Context context) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        movies = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(v);
    }

    public void addMovies(List<SingleMovie> newMovs) {
        movies.addAll(newMovs);
        notifyDataSetChanged();
    }

    public void clearItems() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SingleMovie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.bookmarkButton.setOnClickListener(v ->
        {
            if (!(movie.isInFavorites())) {
                holder.bookmarkButton.setClickable(false);
                Single.fromCallable(() -> {//todo leak
                    return context.getContentResolver().insert(CONTENT_URI,
                            (new MakeContentValues().makeContentValues(movies.get(position)))); //todo class optimization
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(uri -> {
                            ((MainActivity) context).bookmarkAddedCallback(uri);
                            movie.setInFavorites(true);
                            holder.bookmarkButton.setImageResource(R.drawable.bookmarked);
                            holder.bookmarkButton.setClickable(true);
                        });
            } else {
                holder.bookmarkButton.setClickable(false);
                Single.fromCallable(() -> { //todo LEAK!
                    ContentResolver contentResolver = context.getContentResolver();
                    return contentResolver.delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                            new String[]{(new MakeContentValues().makeContentValues(movies.get(position))).getAsString(COLUMN_TITLE)});
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(rowsDeleted -> {
                            ((MainActivity) context).bookmarkDeletedCallback(rowsDeleted);
                            holder.bookmarkButton.setImageResource(R.drawable.unbookmarked);
                            movie.setInFavorites(false);
                            holder.bookmarkButton.setClickable(true);
                        });
            }
        });

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .error(R.drawable.broken_image) //todo: tests in the middle of smth / maybe empty drawable?
                .into(holder.poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });

        if (FavoritesChecker.isFavorite(context, movie)) {
            Picasso.with(context)
                    .load(R.drawable.bookmarked)
                    .into(holder.bookmarkButton);
        } else {
            Picasso.with(context)
                    .load(R.drawable.unbookmarked)
                    .into(holder.bookmarkButton);
        }

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.IDENTIFICATION, movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (movies == null) ? 0 : movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        protected ImageView poster;
        @BindView(R.id.movie_title)
        protected TextView title;
        @BindView(R.id.movie_item_progress)
        protected ProgressBar progressBar;
        @BindView(R.id.favorite_icon)
        protected ImageButton bookmarkButton;
        private final View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setMinimumWidth((int) (dpWidth / 2));
            view.setMinimumHeight((int) ((dpHeight / 2) * 1.5));
            ButterKnife.bind(this, itemView);
        }
    }
}
