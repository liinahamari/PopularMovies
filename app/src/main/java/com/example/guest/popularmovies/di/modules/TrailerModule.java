package com.example.guest.popularmovies.di.modules;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.di.scope.PerFragment;
import com.example.guest.popularmovies.mvp.view.DetailView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 3/29/18.
 */

@Module
public class TrailerModule {
    private DetailView view;

    public TrailerModule (DetailView view) {
        this.view = view;
    }

    @PerFragment
    @Provides
    MovDbApi provideApiService(Retrofit retrofit) {
        return retrofit.create(MovDbApi.class);
    }

    @PerFragment
    @Provides
    DetailView provideView() {
        return view;
    }
}
