package com.example.guest.popularmovies.api;

import com.example.guest.popularmovies.data.model.MoviesArray;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by l1maginaire on 3/1/18.
 */

public interface MdbApi {
    @GET("3/movie/top_rated?api_key=f9a771d5870087b32be1a05bcb8ef697")
    Single<MoviesArray> getDefault();
}
