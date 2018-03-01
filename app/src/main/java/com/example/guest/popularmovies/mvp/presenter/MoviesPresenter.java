package com.example.guest.popularmovies.mvp.presenter;

import com.example.guest.popularmovies.api.MdbApi;
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

public class MoviesPresenter extends BasePresenter<MainView> implements Observer<MoviesArray> {

    @Inject
    protected MdbApi apiService;
//    @Inject
//    protected UsersMapper mapper;

    @Inject
    public MoviesPresenter() {
    }

    public void getEmps() {
        Observable<MoviesArray> empsResponseObservable = apiService.getPopular();
        subscribe(empsResponseObservable, this);
    }

    @Override
    public void onError(Throwable e) {
        getView().onShowToast("Error loading employees: " + e.getMessage());
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(MoviesArray response) {
//        List<SingleMovie> movies = mapper.mapMovies(response);
        getView().onClearItems();
//        getView().onMoviesLoaded(movies);
    }
}
