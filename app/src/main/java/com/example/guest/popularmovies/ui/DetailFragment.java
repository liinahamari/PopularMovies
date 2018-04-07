package com.example.guest.popularmovies.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.adapters.ReviewsAdapter;
import com.example.guest.popularmovies.base.BaseFragment;
import com.example.guest.popularmovies.di.components.DaggerTrailerComponent;
import com.example.guest.popularmovies.di.modules.TrailerModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.model.reviews.Review;
import com.example.guest.popularmovies.mvp.model.trailers.Result;
import com.example.guest.popularmovies.mvp.presenter.DetailPresenter;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l1maginaire on 4/6/18.
 */

public class DetailFragment extends BaseFragment implements DetailView, YouTubePlayer.OnInitializedListener {
    public static final String IDENTIFICATION = "extra_movie";

    @Inject
    protected DetailPresenter presenter;

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

    private YouTubePlayer player;
    private YouTubePlayerSupportFragment playerFragment;
    private List<Result> trailers;
    private ReviewsAdapter reviewsAdapter;
    private SingleMovie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getActivity().getIntent().getParcelableExtra(IDENTIFICATION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
        setupAdapter();
        loadData(String.valueOf(movie.getId()), playerFragment);
        setView();
        return v;
    }

    private void setView() {
        releaseDateTv.setText(movie.getReleaseDate());
        ratingTv.setText(String.valueOf(movie.getVoteAverage()));
        synopsisTv.setText(movie.getOverview());
        titleTv.setText(movie.getTitle());
    }

    @Override
    protected void init() {
        playerFragment = (YouTubePlayerSupportFragment) getFragmentManager().findFragmentById(R.id.youtube_frame);
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

    private void loadData(String id, YouTubePlayerSupportFragment fragment) {
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
    public void onDetach() {
        super.onDetach();
        presenter.unsubscribe();
    }
}
