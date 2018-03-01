package com.example.guest.popularmovies.mvp.view;

import com.example.guest.popularmovies.base.BaseView;

/**
 * Created by l1maginaire on 3/1/18.
 */

public interface MainView extends BaseView {
    void onEmpsLoaded(List<SingleMovie> movies);
    void onShowToast(String message);
    void onClearItems();
}
