package com.example.guest.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.guest.popularmovies.mvp.model.SingleMovie;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_BACKDROP_PATH;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_GENRE_IDS;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_MOV_ID;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_ORIGINAL_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_OVERVIEW;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_POPULARITY;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_POSTER_PATH;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_RELEASE_DATE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_VOTE_AVERAGE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_VOTE_COUNT;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

/**
 * Created by l1maginaire on 4/24/18.
 */

public class DbOperations {//todo somehow inject context

    public static Cursor getAll(Context context) {
        return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    public static int delete(String title, Context context){
        return context.getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                new String[]{title});
    }

    public static Uri insert(SingleMovie movie, Context context){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOV_ID, movie.getId());
        values.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(COLUMN_GENRE_IDS, TextUtils.join(",", movie.getGenreIds()));
        values.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(COLUMN_OVERVIEW, movie.getOverview());
        values.put(COLUMN_POPULARITY, movie.getPopularity());
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(COLUMN_VOTE_COUNT, movie.getVoteCount());
        return context.getContentResolver().insert(CONTENT_URI, values);
    }
}
