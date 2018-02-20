package com.example.guest.popularmovies.interfaces;

/**
 * Created by guest on 2/20/18.
 */

import com.example.guest.popularmovies.BuildConfig;
import com.example.guest.popularmovies.data.model.MoviesArray;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Single;

public interface MovieDbApi {
    static final String API_KEY = BuildConfig.API_KEY;

    @GET("3/movie/top_rated"+API_KEY)
    Single<MoviesArray> getDefault(/*@Query("api_key") int limit, @Query("api-key") String key, @Query("offset") int offset*/);
}