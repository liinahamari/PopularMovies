package com.example.guest.popularmovies.di.components;

import com.example.guest.popularmovies.di.modules.TrailerModule;
import com.example.guest.popularmovies.di.scope.PerActivity;
import com.example.guest.popularmovies.ui.DetailFragment;

import dagger.Component;

/**
 * Created by l1maginaire on 3/29/18.
 */
@PerActivity
@Component(modules = TrailerModule.class, dependencies = ApplicationComponent.class)
public interface TrailerComponent {
    void inject(DetailFragment fragment);
}
