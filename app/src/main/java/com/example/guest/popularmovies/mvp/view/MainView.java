package com.example.guest.popularmovies.mvp.view;

import android.net.Uri;

import com.example.guest.popularmovies.base.BaseView;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.List;

/**
 * Created by l1maginaire on 3/1/18.
 */

public interface MainView extends BaseView {
    void onMoviesLoaded(List<SingleMovie> movies);
    void onClearItems();
}
