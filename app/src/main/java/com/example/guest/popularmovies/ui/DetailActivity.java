package com.example.guest.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

public class DetailActivity extends BaseActivity implements DetailFragment.Callbacks {
    public static final String IDENTIFICATION = "extra_movie";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    public static Intent newIntent(Context context, SingleMovie movie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(IDENTIFICATION, movie);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment;
    }

    @Override
    protected Fragment getMainFragment() {
        return DetailFragment.newInstance(getIntent().getParcelableExtra(IDENTIFICATION));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
