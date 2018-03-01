package com.example.guest.popularmovies.base;

/**
 * Created by l1maginaire on 3/1/18.
 */

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends BaseView> {

    @Inject
    protected V view;

    protected V getView() {
        return view;
    }

    protected <T> void subscribe(Observable<T> observable, Observer<T> observer) {
        observable
                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}