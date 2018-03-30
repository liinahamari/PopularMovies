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
                values.put(COLUMN_MOV_ID, movie.getTitle());
            values.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
//        values.put(GENRE_IDS, movie.getGenreIds());
//        values.put(IN_FAVORITES, true); //todo
            values.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            values.put(COLUMN_OVERVIEW, movie.getOverview());
            values.put(COLUMN_POPULARITY, movie.getPopularity());
            values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
            values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            values.put(COLUMN_VOTE_COUNT, movie.getVoteCount());
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
                            movie.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_MOV_ID)))); //todo catch error
                            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH)));
                            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE)));
                            movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
                            movie.setPopularity(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_POPULARITY))));
                            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
                            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                            movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE))));
                            movie.setVoteCount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_COUNT))));

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
                COLUMN_MOV_ID + " INTEGER, " +
                COLUMN_BACKDROP_PATH + " TEXT, " +
//                GENRE_IDS + " TEXT, " +
//                IN_FAVORITES + " INTEGER, " +
                COLUMN_ORIGINAL_TITLE + " TEXT, " +
                COLUMN_OVERVIEW + " TEXT, " +
                COLUMN_POPULARITY + " DOUBLE, " +
                COLUMN_POSTER_PATH + " TEXT, " +
                COLUMN_RELEASE_DATE + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_VOTE_AVERAGE + " DOUBLE, " +
                COLUMN_VOTE_COUNT + " INTEGER" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}