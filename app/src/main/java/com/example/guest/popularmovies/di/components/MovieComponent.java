package com.example.guest.popularmovies.di.components;

/**
 * Created by guest on 2/20/18.
 */

import com.example.guest.popularmovies.di.modules.MovieModule;
import com.example.guest.popularmovies.di.scope.PerActivity;
import com.example.guest.popularmovies.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(modules = MovieModule.class, dependencies = ApplicationComponent.class)
public interface MovieComponent {
    void inject(MainActivity activity);
}