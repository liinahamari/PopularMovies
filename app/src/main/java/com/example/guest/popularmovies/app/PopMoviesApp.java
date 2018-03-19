package com.example.guest.popularmovies.app;

import android.app.Application;

import com.example.guest.popularmovies.di.components.ApplicationComponent;
import com.example.guest.popularmovies.di.components.DaggerApplicationComponent;
import com.example.guest.popularmovies.di.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by l1maginaire on 3/1/18.
 */

public class PopMoviesApp extends Application {
    private ApplicationComponent applicationComponent; 

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        initializeAppComponent();
    }

    private void initializeAppComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
