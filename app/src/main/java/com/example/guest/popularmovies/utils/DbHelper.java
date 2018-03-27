package com.example.guest.popularmovies.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.guest.popularmovies.db.MoviesContract.Entry;

/**
 * Created by l1maginaire on 3/22/18.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movdb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //todo CHECK NOT NULL
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + Entry.TABLE_NAME + " (" +
//                Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Entry._ID + " INTEGER NOT NULL," +
                Entry.MOV_ID + " INTEGER NOT NULL," +
                Entry.BACKDROP_PATH + " TEXT NOT NULL, " +
                Entry.GENRE_IDS + " TEXT NOT NULL, " +
                Entry.IN_FAVORITES + " INTEGER NOT NULL, " +
                Entry.ORIGINAL_TITLE + " TEXT NOT NULL, " +
                Entry.OVERVIEW + " TEXT NOT NULL, " +
                Entry.POPULARITY + " DOUBLE NOT NULL, " +
                Entry.POSTER_PATH + " TEXT NOT NULL, " +
                Entry.RELEASE_DATE + " TEXT NOT NULL, " +
                Entry.TITLE + " TEXT NOT NULL, " +
                Entry.VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                Entry.VOTE_COUNT + " INTEGER NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}