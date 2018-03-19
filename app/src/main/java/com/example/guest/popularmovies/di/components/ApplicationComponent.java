package com.example.guest.popularmovies.di.components;

import com.example.guest.popularmovies.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by l1maginaire on 3/1/18.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Retrofit exposeRetrofit();
}