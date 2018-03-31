package com.example.guest.popularmovies.utils;

/**
 * Created by l1maginaire on 2/20/18.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.ui.DetailActivity;
import com.example.guest.popularmovies.utils.pagination.MakeContentValues;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<SingleMovie> movies = new ArrayList<>();
    private Context context;
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

    public void clearItems() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SingleMovie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.bookmarkButton.setOnClickListener(v -> {
            if (!(movie.isInFavorites())) //todo другая проверка. возможно, нужно изменить автоинкремент на mov_id
                 {
                Single.fromCallable(() -> {
                    ContentResolver contentResolver = context.getContentResolver();
                    Uri returnUri = contentResolver.insert(CONTENT_URI,
                            (new MakeContentValues().makeContentValues(movies.get(position))));
                    return returnUri;
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(); //todo toast callback
                holder.bookmarkButton.setImageResource(R.drawable.bookmark);
                movie.setInFavorites(true);
            } else {
                Single.fromCallable(() -> {
                    ContentResolver contentResolver = context.getContentResolver();
                    int rowsDeleted = contentResolver.delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                            new String[]{(new MakeContentValues().makeContentValues(movies.get(position))).getAsString(COLUMN_TITLE)});
                    return rowsDeleted;
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                holder.bookmarkButton.setImageResource(R.drawable.unbookmarked);
                movie.setInFavorites(false);
            }
        });
        Picasso
                .with(context)
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        protected ImageView poster;
        @BindView(R.id.movie_title)
        protected TextView title;
        @BindView(R.id.movie_item_progress)
        protected ProgressBar progressBar;
        @BindView(R.id.movie_to_bookmarks)
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
