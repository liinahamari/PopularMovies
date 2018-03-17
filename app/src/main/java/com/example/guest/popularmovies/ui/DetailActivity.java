package com.example.guest.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class DetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {
    public static final String IDENTIFICATION = "extra_movie";

    @BindView(R.id.d_poster)
    protected ImageView poster;
    @BindView(R.id.flexible_example_fab)
    protected View mFab;
    @BindView(R.id.my_collapsing_toolbar)
    protected CollapsingToolbarLayout toolbar;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
//    @BindView(R.id.d_mov_synopsis) protected TextView synopsis;
//    @BindView(R.id.d_mov_rating) protected TextView rating;
//    @BindView(R.id.d_mov_release_date) protected TextView releaseDate;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        getSupportActionBar().hide(); //todo костыль
        SingleMovie movie = getIntent().getParcelableExtra(IDENTIFICATION);
        Picasso.with(this).load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .into(poster);
//        title.setText(movie.getTitle());
//        synopsis.setText(movie.getOverview());
//        rating.setText(String.valueOf(movie.getVoteAverage()));
//        releaseDate.setText(movie.getReleaseDate());

        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        toolbar.setTitle(movie.getTitle());
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.flexible_example_appbar);
        appbar.addOnOffsetChangedListener(this);
    }

    private void loadBackdrop(SingleMovie backdrops) {
//        Picasso.with(this).load()
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail;
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }
}
