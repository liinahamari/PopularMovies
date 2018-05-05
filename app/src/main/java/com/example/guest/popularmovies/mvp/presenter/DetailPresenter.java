package com.example.guest.popularmovies.mvp.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.guest.popularmovies.BuildConfig;
import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.trailers.MovieTrailers;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.example.guest.popularmovies.utils.RxThreadManager;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by l1maginaire on 3/29/18.
 */

public class DetailPresenter extends BasePresenter<DetailView> {
    private final static String TAG = "DetailPresenter";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MovDbApi apiService;

    @Inject
    DetailPresenter() {}

    public void getTrailers(String id, YouTubePlayerSupportFragment youTubePlayerSupportFragment,
                            YouTubePlayer.OnInitializedListener listener, FragmentManager fragmentManager) {
        apiService.getTrailers(id)
                .compose(RxThreadManager.manageObservable())
                .subscribe(new Observer<MovieTrailers>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(MovieTrailers movieTrailers) {
                        getView().onTrailersLoaded(movieTrailers.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to get trailers data: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        youTubePlayerSupportFragment.initialize(BuildConfig.YOUTUBE_KEY, listener);
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.youtube_frame, youTubePlayerSupportFragment).commit();
                    }
                });
    }

    public void getReviews(String id) {
        compositeDisposable.add(apiService.getMovieReviews(id)
                .compose(RxThreadManager.manageObservable())
                .subscribe(movieReviews -> getView().onReviewsLoaded(movieReviews.getReviews())));
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}