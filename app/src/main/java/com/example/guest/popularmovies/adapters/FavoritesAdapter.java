package com.example.guest.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.db.MoviesDbHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_POSTER_PATH;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.TABLE_NAME;

/**
 * Created by l1maginaire on 4/20/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private Cursor cursor;
    private Context context;
    private MoviesDbHelper dbHelper;
    private FrameLayout emptyFavoritesFrame;

    public FavoritesAdapter(Context context, Cursor cursor, FrameLayout emptyFavoritesFrame) {
        this.context = context;
        this.cursor = cursor;
        dbHelper = new MoviesDbHelper(context);
        this.emptyFavoritesFrame = emptyFavoritesFrame;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
        holder.title.setText(title);
        holder.progressBar.setVisibility(View.INVISIBLE);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)))
                .error(R.drawable.empty)
                .into(holder.poster);
        holder.bookmarkButton.setImageResource(R.drawable.bookmarked);
        holder.bookmarkButton.setOnClickListener(v -> {
            Single.fromCallable(() -> context.getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                    new String[]{title}))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(rowsDeleted -> swapCursor(dbHelper.getReadableDatabase()
                            .rawQuery("SELECT * FROM " + TABLE_NAME, null)));
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    private void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
        if(getItemCount()==0)
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