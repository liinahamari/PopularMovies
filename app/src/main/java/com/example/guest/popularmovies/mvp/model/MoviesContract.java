package com.example.guest.popularmovies.mvp.model;

import android.provider.BaseColumns;

/**
 * Created by l1maginaire on 3/22/18.
 */

public class MoviesContract {
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME = "movdb";
        public static final String VOTE_COUNT = "votecount";
        public static final String VOTE_AVERAGE = "voteaverage";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "posterpath";
        public static final String ORIGINAL_TITLE = "otitle";
        public static final String GENRE_IDS = "genres";
        public static final String BACKDROP_PATH = "backdpath";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "date";
        public static final String IN_FAVORITES = "favor";
    }
}
