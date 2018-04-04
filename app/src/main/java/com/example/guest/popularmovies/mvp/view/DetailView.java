package com.example.guest.popularmovies.mvp.view;

import com.example.guest.popularmovies.base.BaseView;
import com.example.guest.popularmovies.mvp.model.trailers.Result;

import java.util.List;

/**
 * Created by l1maginaire on 3/29/18.
 */

public interface DetailView extends BaseView {
    void onTrailersLoaded(List<Result> trailers);
}
