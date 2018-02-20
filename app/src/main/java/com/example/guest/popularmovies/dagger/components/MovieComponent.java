package com.example.guest.popularmovies.dagger.components;

/**
 * Created by guest on 2/20/18.
 */

import com.example.guest.popularmovies.dagger.modules.MoviesModule;
import com.example.guest.popularmovies.interfaces.ApplicationScope;
import com.example.guest.popularmovies.interfaces.MovieDbApi;

import dagger.Component;

@ApplicationScope
@Component(modules = {MoviesModule.class})
public interface MovieComponent{

    MovieDbApi getMovieService();
}