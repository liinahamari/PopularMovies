package com.example.guest.popularmovies.utils;

import android.database.Cursor;

import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * Created by guest2 on 4/21/2018.
 */

public class MakeSMovieByQuery {
    public static SingleMovie makeMovieFromQuery(Cursor cursor){
        SingleMovie movie = new SingleMovie();
        ArrayList<Integer> genreIds = new ArrayList<>();
        List<String> list = Arrays.asList(cursor.getString(cursor.getColumnIndex(COLUMN_GENRE_IDS)).split("\\s*,\\s*"));
        for (String s : list) {
            genreIds.add(Integer.valueOf(s));
        }
        movie.setGenreIds(genreIds);
        movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_MOV_ID))));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
        movie.setPopularity(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_POPULARITY))));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE))));
        movie.setVoteCount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_COUNT))));
        return movie;
    }
}
