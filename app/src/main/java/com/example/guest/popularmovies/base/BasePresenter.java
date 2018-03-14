package com.example.guest.popularmovies.base;

/**
 * Created by l1maginaire on 3/1/18.
 */

import com.example.guest.popularmovies.mvp.model.MoviesArray;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends BaseView> {

    @Inject
    protected V view;

    protected V getView() {
        return view;
    }

    protected Disposable subscribe(Observable<MoviesArray> observable, io.reactivex.functions.Consumer<MoviesArray> onNext,
                   Consumer<Throwable> onError, Action onComplete) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }
}