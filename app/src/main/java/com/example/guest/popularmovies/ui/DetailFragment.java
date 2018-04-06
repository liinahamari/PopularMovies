package com.example.guest.popularmovies.ui;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.base.BaseActivity2;
import com.example.guest.popularmovies.base.BaseFragment;
import com.example.guest.popularmovies.di.components.DaggerTrailerComponent;
import com.example.guest.popularmovies.di.modules.TrailerModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.model.reviews.Review;
import com.example.guest.popularmovies.mvp.model.trailers.Result;
import com.example.guest.popularmovies.mvp.presenter.DetailPresenter;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.example.guest.popularmovies.adapters.ReviewsAdapter;
import com.example.guest.popularmovies.utils.MakeContentValues;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_GENRE_IDS;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_MOV_ID;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_ORIGINAL_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

/**
 * Created by l1maginaire on 4/6/18.
 */

public class DetailFragment extends BaseFragment implements DetailView, AppBarLayout.OnOffsetChangedListener,
        YouTubePlayer.OnInitializedListener{
    public static final String IDENTIFICATION = "extra_movie";

    @Inject
    protected DetailPresenter presenter;

    @BindView(R.id.d_poster)
    protected ImageView posterIv;
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
    @BindView(R.id.reviews_recycler)
    protected RecyclerView reviewsRecyclerView;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private YouTubePlayer player;
    private List<Result> trailers;
    private ReviewsAdapter reviewsAdapter;
    private SingleMovie movie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        setupAdapter();
        if (savedInstanceState != null) {
            onMoviesLoaded(savedInstanceState.getParcelableArrayList(SAVED_LIST));
            recyclerView.scrollToPosition(lastVisiblePosition);
        } else {
            loadNew();
        }
        return v;
    }

    private void setView() {
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .into(posterIv);
        releaseDateTv.setText(movie.getReleaseDate());
        ratingTv.setText(String.valueOf(movie.getVoteAverage()));
        synopsisTv.setText(movie.getOverview());
        titleTv.setText(movie.getTitle());
    }

    @Override
    protected void init() {

    }

    @Override
    protected void resolveDaggerDependencies() {
        DaggerTrailerComponent.builder()
                .applicationComponent(getApplicationComponent(getActivity()))
                .trailerModule(new TrailerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onTrailersLoaded(List<Result> trailers) {
        this.trailers = trailers;
/*
        Cursor c = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String s2 = c.getString(c.getColumnIndex(COLUMN_ORIGINAL_TITLE));
                String s3 = String.valueOf(c.getInt(c.getColumnIndex(COLUMN_MOV_ID)));
                String s = c.getString(c.getColumnIndex(COLUMN_GENRE_IDS));
            } while (c.moveToNext());
            c.close();
        }
*/
    }

    private void setupListeners() {
        floatingButton.setBackgroundTintList((movie.isInFavorites()!=0) ?
                (ColorStateList.valueOf(getResources().getColor(R.color.colorAccent))) : //todo check <21
                ColorStateList.valueOf(getResources().getColor(R.color.lightLight)));
        floatingButton.setOnClickListener(v -> {
            if (movie.isInFavorites()!=0) {
                floatingButton.setClickable(false);
                Single.fromCallable(() -> {
                    return getActivity().getContentResolver().insert(CONTENT_URI, (new MakeContentValues().makeContentValues(movie))); //todo class optimization
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(uri -> {
                            movie.setInFavorites(1);
                            Toast.makeText(getActivity(), movie.getTitle() + " added to Favorites!", Toast.LENGTH_SHORT).show();
                            floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            floatingButton.setClickable(true);
                        });
            } else {
                floatingButton.setClickable(false);
                Single.fromCallable(() -> {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    return contentResolver.delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                            new String[]{(new MakeContentValues().makeContentValues(movie)).getAsString(COLUMN_TITLE)});
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(rowsDeleted -> {
                            movie.setInFavorites(0);
                            Toast.makeText(getActivity(), movie.getTitle() + " removed from Favorites!", Toast.LENGTH_SHORT).show();
                            floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightLight))); //todo check versions <21
                            floatingButton.setClickable(true);
                        });
            }
        });
    }


    private void loadData(String id, YouTubePlayerFragment fragment) {
        presenter.getTrailers(id, fragment, this);
        presenter.getReviews(id);
    }

    private void setupAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        reviewsAdapter = new ReviewsAdapter(getActivity());
        reviewsRecyclerView.setAdapter(reviewsAdapter);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        reviewsAdapter.addReviews(reviews);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        player = youTubePlayer;
        player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION); //todo leak detected
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
//        if (!b) {
        player.cueVideo(trailers.get(0).getKey());
//        } else {
//            player.play();
//        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        player = null;
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

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unsubscribe();
    }
}
