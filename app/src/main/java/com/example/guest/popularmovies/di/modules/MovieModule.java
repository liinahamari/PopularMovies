package com.example.guest.popularmovies.di.modules;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.di.scope.PerActivity;
import com.example.guest.popularmovies.mvp.view.MainView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 3/1/18.
 */

@Module
public class MovieModule {

    private MainView view;

    public MovieModule(MainView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    MovDbApi provideApiService(Retrofit retrofit) {
        return retrofit.create(MovDbApi.class);
    }

    @PerActivity
    @Provides
    MainView provideView() {
        return view;
    }
}