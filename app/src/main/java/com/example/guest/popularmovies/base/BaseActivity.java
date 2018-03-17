package com.example.guest.popularmovies.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
 **/

public abstract class BaseActivity extends AppCompatActivity {
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        int color = (System.currentTimeMillis()%2==0) ? Color.RED : Color.CYAN;
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
        setContentView(getContentView());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        resolveDaggerDependencies();
    }

    protected void showBackArrow() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((PopMoviesApp) getApplication()).getApplicationComponent();
    }

    protected abstract int getContentView();

    protected void resolveDaggerDependencies(){}
}