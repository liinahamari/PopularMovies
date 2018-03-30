package com.example.guest.popularmovies.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;

/**
 * Created by l1maginaire on 3/29/18.
 */

public class DatabaseTasks extends AsyncTask<Object, Void, Void> {

    private final String LOG_TAG = DatabaseTasks.class.getSimpleName();
    private final Context context;
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    public DatabaseTasks(Context context) {
        this.context = context;
    }

    protected Void doInBackground(Object... params) {

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values;

        switch ((Integer) params[0]) {
            case INSERT:
                values = (ContentValues) params[1];
                contentResolver.insert(CONTENT_URI, values);
                contentResolver.query(MoviesContract.Entry.CONTENT_URI, null, null, null, null);
                break;
            case UPDATE:
                break;
            case DELETE:
                values = (ContentValues) params[1];
                contentResolver.delete(CONTENT_URI,
                        COLUMN_TITLE + " = ?",
                        new String[]{values.getAsString(COLUMN_TITLE)});
                break;
            default:
                break;
        }
        return null;
    }
}
