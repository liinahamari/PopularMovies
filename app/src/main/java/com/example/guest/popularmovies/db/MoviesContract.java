package com.example.guest.popularmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by l1maginaire on 3/22/18.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.guest.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME = "movdb";
        public static final String COLUMN_MOV_ID = "idmov";
        public static final String COLUMN_VOTE_COUNT = "votecount";
        public static final String COLUMN_VOTE_AVERAGE = "voteaverage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_ORIGINAL_TITLE = "otitle";
        public static final String COLUMN_GENRE_IDS = "genres";
        public static final String COLUMN_BACKDROP_PATH = "backdpath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "date";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    }
}
