package com.example.guest.popularmovies.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.guest.popularmovies.app.PopMoviesApp;
import com.example.guest.popularmovies.di.components.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * Created by l1maginaire on 3/1/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    private void onViewReady(Bundle savedInstanceState, Intent intent) {
        resolveDaggerDependencies();
    }

    protected void showBackArrow() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((PopMoviesApp) getApplication()).getApplicationComponent();
    }

    protected abstract int getContentView();

    protected void resolveDaggerDependencies(){}
}