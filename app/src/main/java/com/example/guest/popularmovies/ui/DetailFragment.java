package com.example.guest.popularmovies.ui;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.adapters.ReviewsPagerAdapter;
import com.example.guest.popularmovies.base.BaseFragment;
import com.example.guest.popularmovies.di.components.DaggerTrailerComponent;
import com.example.guest.popularmovies.di.modules.TrailerModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.model.reviews.Review;
import com.example.guest.popularmovies.mvp.model.trailers.Result;
import com.example.guest.popularmovies.mvp.presenter.DetailPresenter;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.example.guest.popularmovies.utils.LikeButtonColorChanger;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by l1maginaire on 4/6/18.
 */

public class DetailFragment extends BaseFragment implements DetailView, YouTubePlayer.OnInitializedListener,
        AppBarLayout.OnOffsetChangedListener {
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
    @BindView(R.id.youtube_frame)
    protected FrameLayout youtubeFrame;
    @BindView(R.id.pager)
    protected ViewPager viewPager;
    @BindView(R.id.reviews_label)
    protected TextView reviewsLabel;
    @BindView(R.id.mytoolbar)
    protected Toolbar toolbar;
    @BindView(R.id.my_appbar)
    protected AppBarLayout appbar;
    @BindView(R.id.fab)
    protected FloatingActionButton fab;
    @BindView(R.id.my_collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.d_poster)
    protected ImageView posterIv;
    @BindView(R.id.tab_dots)
    protected TabLayout dots;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 40;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private YouTubePlayer player;
    private List<Result> trailers;
    private SingleMovie movie;
    private ReviewsPagerAdapter pagerAdapter;
    private Callbacks callbacks;
    private int orientation = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callbacks = (DetailFragment.Callbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onLikeClicked()");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orientation = getActivity().getResources().getConfiguration().orientation;
    }

    public static DetailFragment newInstance(SingleMovie movie) {
        Bundle args = new Bundle();
        args.putParcelable(IDENTIFICATION, movie);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable(IDENTIFICATION);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;
                ViewCompat.animate(fab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(fab).scaleY(1).scaleX(1).start();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
        setupListeners();
        viewPager.setAdapter(pagerAdapter);
        dots.setupWithViewPager(viewPager, true);
        loadData(String.valueOf(movie.getId()));
        setView();
        return v;
    }

    private void setupListeners() {
        fab.setOnClickListener(v -> callbacks.onLikeClicked(movie, fab));
        if (getActivity() != null && getActivity().getLocalClassName().equals("ui.DetailActivity")) {
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            appbar.addOnOffsetChangedListener(this);
        } else {
            toolbar.setNavigationIcon(null);
            ((MainActivity) getActivity()).setFab(fab, movie);
        }
    }

    private void setView() {
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .into(posterIv);
        releaseDateTv.setText(movie.getReleaseDate());
        ratingTv.setText(String.valueOf(movie.getVoteAverage()));
        synopsisTv.setText(movie.getOverview());
        titleTv.setText(movie.getTitle());
        LikeButtonColorChanger.change(fab, getActivity(), movie.isInFavorites());
    }

    @Override
    protected void init() {
        pagerAdapter = new ReviewsPagerAdapter(getChildFragmentManager());
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
    }

    private void loadData(String id) {
        presenter.getTrailers(id, new YouTubePlayerSupportFragment(), this, getFragmentManager());
        presenter.getReviews(id);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        if (reviews.size() == 0) {
            reviewsLabel.setVisibility(GONE);
            viewPager.setVisibility(GONE);
        } else {
            pagerAdapter.setData(reviews);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean isRotated) {
        player = youTubePlayer;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE || getResources().getBoolean(R.bool.isTab)) {
            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (getActivity() != null && getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            ViewGroup.LayoutParams layoutParams = youtubeFrame.getLayoutParams();
            layoutParams.height = (3 * actionBarHeight);
            layoutParams.width = ((getActivity().getResources().getDisplayMetrics().widthPixels) - (2 * actionBarHeight));
            youtubeFrame.setLayoutParams(layoutParams);
        }
        player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        if (trailers.size() > 0) {
            player.cueVideo(trailers.get(0).getKey());
        }
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

    public interface Callbacks {
        void onLikeClicked(SingleMovie movie, FloatingActionButton floatingButton);
    }
}
