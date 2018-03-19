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
    protected ImageView posterIv;
    @BindView(R.id.fab)
    protected View floatingButton;
    @BindView(R.id.my_collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.d_mov_rate)
    protected TextView ratingTv;
    @BindView(R.id.d_mov_date)
    protected TextView releaseDateTv;
    @BindView(R.id.d_mov_synopsis)
    protected TextView synopsisTv;
    @BindView(R.id.d_mov_title)
    protected TextView titleTv;
    @BindView(R.id.mytoolbar)
    protected Toolbar toolbar;
    @BindView(R.id.my_appbar)
    protected AppBarLayout appbar;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        SingleMovie movie = getIntent().getParcelableExtra(IDENTIFICATION);
        setActionBarView(movie);

        Picasso.with(this).load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .into(posterIv);
        releaseDateTv.setText(movie.getReleaseDate());
        ratingTv.setText(String.valueOf(movie.getVoteAverage()));
        synopsisTv.setText(movie.getOverview());
        titleTv.setText(movie.getTitle());
    }

    private void setActionBarView(SingleMovie movie) {
        getSupportActionBar().hide();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        appbar.addOnOffsetChangedListener(this);
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

                ViewCompat.animate(floatingButton).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(floatingButton).scaleY(1).scaleX(1).start();
            }
        }
    }
}
