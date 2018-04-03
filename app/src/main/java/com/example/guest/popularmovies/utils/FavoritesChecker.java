package com.example.guest.popularmovies.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.example.guest.popularmovies.mvp.model.SingleMovie;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.db.MoviesContract.Entry._ID;

/**
 * Created by l1maginaire on 4/3/18.
 */

public class FavoritesChecker { //todo допустимость использования в MainThread >?<
    public boolean isFavorite(Context context, SingleMovie movie) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = null;
        if (movie.getId() != 0) {
            c = contentResolver.query(CONTENT_URI,
                    null,
                    _ID + " = ?",
                    new String[]{String.valueOf(movie.getId())},
                    null);
        }
        if (c != null) {
            c.moveToFirst();
            int index = c.getColumnIndex(_ID);

            if (c.getCount() > 0 && c.getLong(index) == movie.getId()) {
                c.close();
                return true;
            }
            c.close();
        }
        return false;
    }
}
