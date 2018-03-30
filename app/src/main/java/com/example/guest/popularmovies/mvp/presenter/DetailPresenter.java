package com.example.guest.popularmovies.mvp.presenter;

import com.example.guest.popularmovies.BuildConfig;
import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MovieTrailers;
import com.example.guest.popularmovies.mvp.view.DetailView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by l1maginaire on 3/29/18.
 */

public class DetailPresenter extends BasePresenter<DetailView> {
    @Inject
    protected MovDbApi apiService;
    private Disposable disposable;

    @Inject
    public DetailPresenter() {
    }

    /*public void getTrailers(Integer id) {
        Observable<MovieTrailers> observable = apiService.getTrailers(id);
        disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    getView().onTrailersLoaded(response.getResults());
                }, throwable -> {
                }, () -> {
                });
    }*/

    public void unsubscribe() {
        if (disposable != null)
            disposable.dispose();
    }

    public void getTrailers(String id, YouTubePlayerFragment fragment, YouTubePlayer.OnInitializedListener listener){
        /*disposable = todo*/apiService.getTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieTrailers>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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
}