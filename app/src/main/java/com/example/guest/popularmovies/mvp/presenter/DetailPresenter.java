package com.example.guest.popularmovies.mvp.presenter;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MovieTrailers;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.Trailer;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by l1maginaire on 3/29/18.
 */

public class DetailPresenter extends BasePresenter<MainView> {
    private PaginationTool<MoviesArray> paginationTool;

    @Inject
    protected MovDbApi apiService;

    @Inject
    public DetailPresenter() {
    }

    public void getTrailers(String id) {
        Observable<MovieTrailers> observable = apiService.getTrailers(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Trailer> movies = response.getTrailers();
//                  getView().onClearItems();
//                  getView().onMoviesLoaded(movies);
                }, throwable -> {
                }, () -> {
                });
    }
}