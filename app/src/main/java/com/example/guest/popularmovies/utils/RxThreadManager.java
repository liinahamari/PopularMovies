package com.example.guest.popularmovies.utils;

/**
 * Created by l1maginaire on 5/3/18.
 */

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxThreadManager {
    public static <T> ObservableTransformer<T, T> manage() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
