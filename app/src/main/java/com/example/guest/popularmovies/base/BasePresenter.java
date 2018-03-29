package com.example.guest.popularmovies.base;

/**
 * Created by l1maginaire on 3/1/18.
 */

import javax.inject.Inject;

public class BasePresenter<V extends BaseView> {

    @Inject
    protected V view;

    protected V getView() {
        return view;
    }
}