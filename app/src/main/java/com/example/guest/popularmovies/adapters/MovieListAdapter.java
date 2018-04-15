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

    public void setFab(FloatingActionButton fab){
        this.fab = fab;
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
            if (movie.isInFavorites() == 0) {
                holder.bookmarkButton.setClickable(false);
                Single.fromCallable(() -> context.getContentResolver().insert(CONTENT_URI, (makeContentValues(movies.get(position)))))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(uri -> {
                            movie.setInFavorites(1);
                            holder.bookmarkButton.setImageResource(R.drawable.bookmarked);
                            holder.bookmarkButton.setClickable(true);
                            if(fab!=null){
                                if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                                    fab.setBackgroundTintList(ColorStateList.valueOf
                                            (context.getResources().getColor(R.color.colorAccent)));
                                } else {
                                    fab.getBackground().setColorFilter
                                            (context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                                }
                            }
                        });
            } else {
                holder.bookmarkButton.setClickable(false);
                Single.fromCallable(() -> context.getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                        new String[]{(makeContentValues(movies.get(position))).getAsString(COLUMN_TITLE)}))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(rowsDeleted -> {
                            movie.setInFavorites(0);
                            holder.bookmarkButton.setImageResource(R.drawable.unbookmarked);
                            holder.bookmarkButton.setClickable(true);
                            if(fab!=null){
                                if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                                    fab.setBackgroundTintList(ColorStateList.valueOf
                                            (context.getResources().getColor(R.color.lightLight)));
                                } else {
                                    fab.getBackground().setColorFilter
                                            (context.getResources().getColor(R.color.lightLight), PorterDuff.Mode.MULTIPLY);
                                }
                            }
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

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setMinimumWidth((int) (dpWidth / 2));
            view.setMinimumHeight((int) ((dpHeight / 2) * 1.5));
            ButterKnife.bind(this, itemView);
        }
    }
}
