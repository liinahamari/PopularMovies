/*
package com.example.guest.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.guest.popularmovies.db.MoviesContract.CONTENT_AUTHORITY;
import static com.example.guest.popularmovies.db.MoviesContract.PATH_MOVIES;

*/
/**
 * Created by l1maginaire on 3/27/18.
 *//*


public class MovieProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MoviesDbHelper dbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_ITEM = 200;

    private static final SQLiteQueryBuilder sMoviesBuilder;

    static{
        sMoviesBuilder = new SQLiteQueryBuilder();
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, PATH_MOVIES, MOVIES);
        matcher.addURI(authority, PATH_MOVIES + "*/
/*", MOVIE_ITEM);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
            case MOVIE_ITEM:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }



}*/
