package com.example.guest.popularmovies.mvp.presenter;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.view.MainView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by l1maginaire on 2/25/18.
 */

public class MoviesPresenter extends BasePresenter<MainView> {
    private Disposable disposable1;
    private Disposable disposable2;

    @Inject
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular(int page) {
        Observable<MoviesArray> observable = apiService.getPopular(page);
        disposable1 = subscribe(observable, response -> {
            List<SingleMovie> movies = response.getResults();
            getView().onClearItems();
            getView().onMoviesLoaded(movies);
        }, Throwable::printStackTrace, () -> {
            // todo: onComplete logging
        });
    }

    public void getTopRated(int page) {
        Observable<MoviesArray> observable = apiService.getTopRated(page);
        disposable2 = subscribe(observable, response -> {
            List<SingleMovie> movies = response.getResults();
            getView().onClearItems();
            getView().onMoviesLoaded(movies);
        }, Throwable::printStackTrace, () -> {
            // todo: onComplete logging
        });
    }

    public void unsubscribe() {
        if (disposable1 != null)
            disposable1.dispose();
        if (disposable2 != null)
            disposable2.dispose();
    }
}
