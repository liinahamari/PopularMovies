package com.example.guest.popularmovies.adapters;

/**
 * Created by l1maginaire on 2/20/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.example.guest.popularmovies.ui.MainFragment;
import com.example.guest.popularmovies.utils.DbOperations;
import com.example.guest.popularmovies.utils.FavoritesChecker;
import com.example.guest.popularmovies.utils.LikeButtonColorChanger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<SingleMovie> movies;
    private Context context;
    private LayoutInflater layoutInflater;
    private MainFragment.Callbacks callbacks;
    private FloatingActionButton fab;
    private int position = -1;
    private int resize = 0;
    private float dpHeight;
    private float dpWidth;

    public MovieListAdapter(LayoutInflater layoutInflater, Context context, MainFragment.Callbacks callbacks) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.callbacks = callbacks;
        movies = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (context.getResources().getBoolean(R.bool.isTab)) {
            resize = 60;
        } else {
            resize = 90;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(v);
    }

    public void addMovies(List<SingleMovie> newMovs) {
        movies.addAll(newMovs);
        notifyDataSetChanged();
    }

    public void setFab(FloatingActionButton fab, int position) {
        this.fab = fab;
        this.position = position;
    }

    public void clearItems() {
        movies.clear();
        notifyDataSetChanged();
    }

    private void bookmarkCallback(SingleMovie movie, int setFavorite, ViewHolder holder, int position) {
        movie.setInFavorites(setFavorite);
        Picasso.with(context)
                .load(setFavorite != 0 ? R.drawable.bookmarked : R.drawable.unbookmarked)
                .resize(resize, resize)
                .into(holder.bookmarkButton);
        if (fab != null && this.position == position) {
            LikeButtonColorChanger.change(fab, context, setFavorite);
        }
        holder.bookmarkButton.setClickable(true);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleMovie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.bookmarkButton.setOnClickListener(v ->
        {
            holder.bookmarkButton.setClickable(false);
            if (movie.isInFavorites() == 0) {
                Single.fromCallable(() -> DbOperations.insert(movies.get(position), context))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(uri -> bookmarkCallback(movie, 1, holder, position));
            } else {
                Single.fromCallable(() -> DbOperations.delete(movies.get(position).getTitle(), context))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(rowsDeleted -> bookmarkCallback(movie, 0, holder, position));
            }
        });

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .error(R.drawable.empty)
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

        Single.fromCallable(() -> {
            holder.bookmarkButton.setClickable(false);
            return FavoritesChecker.isFavorite(context, movie);
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(isFavorite -> {
                    holder.bookmarkButton.setClickable(true);
                    Picasso.with(context)
                            .load(isFavorite != 0 ? R.drawable.bookmarked : R.drawable.unbookmarked)
                            .resize(resize, resize)
                            .into(holder.bookmarkButton);
                    movie.setInFavorites(isFavorite);
                });

        holder.view.setOnClickListener(v -> callbacks.onItemClicked(movie, position));
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

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            /**
             * For remaining appropriate size on fetching errors
             * */
            view.setMinimumWidth((int) (dpWidth / 2));
            view.setMinimumHeight((int) ((dpHeight / 2) * 1.5));

            ButterKnife.bind(this, itemView);
        }
    }
}
