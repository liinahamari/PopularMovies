package com.example.guest.popularmovies.mvp.presenter;

import com.example.guest.popularmovies.BuildConfig;
import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.trailers.MovieTrailers;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by l1maginaire on 3/29/18.
 */

public class DetailPresenter extends BasePresenter<DetailView> {
    @Inject
    MovDbApi apiService;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DetailPresenter() {
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

//todo: 04-03 23:12:06.864 25819-25819/com.example.guest.popularmovies E/ActivityThread: Activity com.example.guest.popularmovies.ui.DetailActivity has leaked ServiceConnection com.google.android.youtube.player.internal.r$e@f7c4314 that was originally bound here

    public void getTrailers(String id, YouTubePlayerFragment fragment, YouTubePlayer.OnInitializedListener listener) {
        apiService.getTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

                    }

                    @Override
                    public void onComplete() {
                        fragment.initialize(BuildConfig.YOUTUBE_KEY, listener);
                    }
                });
    }

    public void getReviews(String id) {
        compositeDisposable.add(apiService.getMovieReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieReviews -> getView().onReviewsLoaded(movieReviews.getReviews())));
    }
}