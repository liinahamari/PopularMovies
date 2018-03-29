package com.example.guest.popularmovies.db;

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
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movdb";
        public static final String MOV_ID = "idmov";
        public static final String VOTE_COUNT = "votecount";
        public static final String VOTE_AVERAGE = "voteaverage";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "posterpath";
        public static final String ORIGINAL_TITLE = "otitle";
//        public static final String GENRE_IDS = "genres";
        public static final String BACKDROP_PATH = "backdpath";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "date";
        public static final String IN_FAVORITES = "favor";

        //todo: 2 methods
    }
}
