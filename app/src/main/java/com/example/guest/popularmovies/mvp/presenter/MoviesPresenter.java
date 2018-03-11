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

public class MoviesPresenter extends BasePresenter<MainView>{

    @Inject
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular() {
        Observable<MoviesArray> observable = apiService.getPopular();
        subscribe(observable, new Observer<MoviesArray>() {
            @Override
            public void onNext(MoviesArray response) {
                List<SingleMovie> movies = response.getResults();
                getView().onClearItems();
                getView().onMoviesLoaded(movies);
            }

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    public void getTopRated() {
        Observable<MoviesArray> observable = apiService.getTopRated();
        subscribe(observable, new Observer<MoviesArray>() {
            @Override
            public void onNext(MoviesArray response) {
                List<SingleMovie> movies = response.getResults();
                getView().onClearItems();
                getView().onMoviesLoaded(movies);
            }

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }
}
