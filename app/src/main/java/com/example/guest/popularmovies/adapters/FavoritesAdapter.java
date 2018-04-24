package com.example.guest.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.ui.MainFragment;
import com.example.guest.popularmovies.utils.DbOperations;
import com.example.guest.popularmovies.utils.LikeButtonColorChanger;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.utils.MakeSMovieByQuery.makeMovieFromQuery;

/**
 * Created by l1maginaire on 4/20/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private static final String TAG = FavoritesAdapter.class.getSimpleName();
    private Cursor cursor;
    private Context context;
    private FrameLayout emptyFavoritesFrame;
    private LayoutInflater layoutInflater;
    private MainFragment.Callbacks callbacks;
    private int resize = 0;
    private String title;
    private FloatingActionButton fab;

    public FavoritesAdapter(Context context, FrameLayout emptyFavoritesFrame, LayoutInflater layoutInflater,
                            MainFragment.Callbacks callbacks) {
        this.layoutInflater = layoutInflater;
        this.callbacks = callbacks;
        this.context = context;
        this.emptyFavoritesFrame = emptyFavoritesFrame;
        if (context.getResources().getBoolean(R.bool.isTab)) {
            resize = 60;
        } else {
            resize = 90;
        }
    }

    public void setFab(FloatingActionButton fab, String title) {
        this.fab = fab;
        this.title = title;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        SingleMovie movie = makeMovieFromQuery(cursor);
        holder.title.setText(movie.getTitle());
        holder.progressBar.setVisibility(View.INVISIBLE);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .error(R.drawable.empty)
                .into(holder.poster);
        Picasso.with(context)
                .load(R.drawable.bookmarked)
                .resize(resize, resize)
                .into(holder.bookmarkButton);
        holder.bookmarkButton.setOnClickListener(v ->
                Single.fromCallable(() -> DbOperations.delete(movie.getTitle(), context))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(rowsDeleted -> {
                                    if (rowsDeleted != 0) {
                                        swapCursor(DbOperations.getAll(context));
                                        syncWithLikeButton(movie.getTitle());
                                    }
                                }
                        ));
        holder.itemView.setOnClickListener(v -> callbacks.onItemClicked(movie, position));
    }

    private void syncWithLikeButton(String title) {
        try {
            if (this.title.equals(title)) {
                LikeButtonColorChanger.change(fab, context, 0);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "DetailFragment wasn't opened");
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
        if (getItemCount() == 0)
            emptyFavoritesFrame.setVisibility(View.VISIBLE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        protected ImageView poster;
        @BindView(R.id.movie_title)
        protected TextView title;
        @BindView(R.id.favorite_icon)
        protected ImageButton bookmarkButton;
        @BindView(R.id.movie_item_progress)
        protected ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}