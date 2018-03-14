package com.example.guest.popularmovies.api;

/**
 * Created by l1maginaire on 3/1/18.
 */

import com.example.guest.popularmovies.BuildConfig;
import com.example.guest.popularmovies.mvp.model.MoviesArray;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by l1maginaire on 3/1/18.
 */

public interface MovDbApi {
    @GET("3/movie/popular?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesArray> getPopular(@Query("page") int page);

    @GET("3/movie/top_rated?api_key=" + BuildConfig.API_KEY)
    Observable<MoviesArray> getTopRated(@Query("page") int page);
}
