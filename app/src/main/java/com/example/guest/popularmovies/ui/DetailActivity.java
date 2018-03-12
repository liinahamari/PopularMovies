package com.example.guest.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {
    public static final String IDENTIFICATION = "extra_movie";

    @BindView(R.id.d_poster) protected ImageView poster;
    @BindView(R.id.d_mov_title) protected TextView title;
    @BindView(R.id.d_mov_synopsis) protected TextView synopsis;
    @BindView(R.id.d_mov_rating) protected TextView rating;
    @BindView(R.id.d_mov_release_date) protected TextView releaseDate;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        showBackArrow();
        SingleMovie movie = (SingleMovie) getIntent().getParcelableExtra(IDENTIFICATION);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .into(poster);
        title.setText(movie.getTitle());
        synopsis.setText(movie.getOverview());
        rating.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());
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
}
