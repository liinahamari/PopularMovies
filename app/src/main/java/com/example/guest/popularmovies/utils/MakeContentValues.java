package com.example.guest.popularmovies.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import com.example.guest.popularmovies.mvp.model.SingleMovie;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.*;

/**
 * Created by l1maginaire on 3/31/18.
 */

public class MakeContentValues {

    public ContentValues makeContentValues(SingleMovie movie) {
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
        return values;
    }
}
