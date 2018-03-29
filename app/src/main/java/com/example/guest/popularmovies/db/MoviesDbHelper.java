package com.example.guest.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.guest.popularmovies.db.MoviesContract.Entry;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.ArrayList;
import java.util.List;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.*;

/**
 * Created by l1maginaire on 3/22/18.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MoviesDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movdb.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addMovies(List<SingleMovie> movies) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        for (SingleMovie movie:movies) {
                values.put(MOV_ID, movie.getTitle());
            values.put(BACKDROP_PATH, movie.getBackdropPath());
//        values.put(GENRE_IDS, movie.getGenreIds());
//        values.put(IN_FAVORITES, true); //todo
            values.put(ORIGINAL_TITLE, movie.getOriginalTitle());
            values.put(OVERVIEW, movie.getOverview());
            values.put(POPULARITY, movie.getPopularity());
            values.put(POSTER_PATH, movie.getPosterPath());
            values.put(RELEASE_DATE, movie.getReleaseDate());
            values.put(VOTE_AVERAGE, movie.getVoteAverage());
            values.put(VOTE_COUNT, movie.getVoteCount());
        }

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
        }
        db.close();
    }


    public List<SingleMovie> getSavedMovies() {
        List<SingleMovie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            SingleMovie movie = new SingleMovie();
                            movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(MOV_ID)))); //todo catch error
                            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(BACKDROP_PATH)));
                            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                            movie.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                            movie.setPopularity(Double.valueOf(cursor.getString(cursor.getColumnIndex(POPULARITY))));
                            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                            movie.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                            movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(VOTE_AVERAGE))));
                            movie.setVoteCount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(VOTE_COUNT))));

                            movies.add(movie);

                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (SQLException e) {
            Log.d(TAG, e.getMessage());
        }
        return movies;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //todo CHECK NOT NULL
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                Entry._ID + " INTEGER, " +
                MOV_ID + " INTEGER, " +
                BACKDROP_PATH + " TEXT, " +
//                GENRE_IDS + " TEXT, " +
//                IN_FAVORITES + " INTEGER, " +
                ORIGINAL_TITLE + " TEXT, " +
                OVERVIEW + " TEXT, " +
                POPULARITY + " DOUBLE, " +
                POSTER_PATH + " TEXT, " +
                RELEASE_DATE + " TEXT, " +
                TITLE + " TEXT, " +
                VOTE_AVERAGE + " DOUBLE, " +
                VOTE_COUNT + " INTEGER" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}