package com.example.guest.popularmovies.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.guest.popularmovies.db.MoviesContract.Entry;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOV_ID + " INTEGER, " +
                COLUMN_BACKDROP_PATH + " TEXT, " +
                COLUMN_GENRE_IDS + " TEXT, " +
                COLUMN_ORIGINAL_TITLE + " TEXT, " +
                COLUMN_OVERVIEW + " TEXT, " +
                COLUMN_POPULARITY + " DOUBLE, " +
                COLUMN_POSTER_PATH + " TEXT, " +
                COLUMN_RELEASE_DATE + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_VOTE_AVERAGE + " DOUBLE, " +
                COLUMN_VOTE_COUNT + " INTEGER" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "Upgrading database... Previous version: " + i + "; actual version: " + i1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}