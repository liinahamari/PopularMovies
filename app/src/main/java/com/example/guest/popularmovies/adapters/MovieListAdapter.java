package com.example.guest.popularmovies.adapters;

/**
 * Created by l1maginaire on 2/20/18.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.utils.FavoritesChecker.isFavorite;
import static com.example.guest.popularmovies.utils.MakeContentValues.makeContentValues;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<SingleMovie> movies;
    private Context context;
    private LayoutInflater layoutInflater;
    private float dpHeight;
    private float dpWidth;
    private MainFragment.Callbacks callbacks;
    private FloatingActionButton fab;
    private int position = -1;

    public MovieListAdapter(LayoutInflater layoutInflater, Context context, MainFragment.Callbacks callbacks) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.callbacks = callbacks;
        movies = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
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
                .resize(90,90)
                .into(holder.bookmarkButton);
        if (fab != null && this.position == position) {
            syncWithLikeButton(setFavorite);
        }
        holder.bookmarkButton.setClickable(true);
    }

    private void syncWithLikeButton(int isFavorite) {
        int color = (isFavorite == 0) ? R.color.lightLight : R.color.colorAccent;
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            fab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
        } else {
            fab.getBackground().setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleMovie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.bookmarkButton.setOnClickListener(v ->
        {
            holder.bookmarkButton.setClickable(false);
            if (movie.isInFavorites() == 0) {
                Single.fromCallable(() -> context.getContentResolver().insert(CONTENT_URI, (makeContentValues(movies.get(position)))))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(uri -> bookmarkCallback(movie, 1, holder, position));
            } else {
                Single.fromCallable(() -> context.getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                        new String[]{(makeContentValues(movies.get(position))).getAsString(COLUMN_TITLE)}))
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
            return isFavorite(context, movie);
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(isFavorite -> {
                    holder.bookmarkButton.setClickable(true);
                    movie.setInFavorites(isFavorite);
                    Picasso.with(context)
                            .load(isFavorite != 0 ? R.drawable.bookmarked : R.drawable.unbookmarked)
                            .resize(90,90)
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
            view.setMinimumWidth((int) (dpWidth / 2));
            view.setMinimumHeight((int) ((dpHeight / 2) * 1.5));
            ButterKnife.bind(this, itemView);
        }
    }
}
