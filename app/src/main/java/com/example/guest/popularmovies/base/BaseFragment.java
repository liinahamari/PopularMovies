package com.example.guest.popularmovies.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.guest.popularmovies.app.PopMoviesApp;
import com.example.guest.popularmovies.di.components.ApplicationComponent;

/**
 * Created by l1maginaire on 4/6/18.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveDaggerDependencies();
        init();
    }

    protected abstract void init();

    protected abstract void resolveDaggerDependencies();

    protected ApplicationComponent getApplicationComponent(Activity activity) {
        return ((PopMoviesApp) activity.getApplication()).getApplicationComponent();
    }

    protected abstract void setupListeners();
}
