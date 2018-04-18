package com.example.guest.popularmovies.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.db.MoviesDbHelper;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;
import com.example.guest.popularmovies.utils.pagination.PagingListener;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by l1maginaire on 2/25/18.
 */

public class MoviesPresenter extends BasePresenter<MainView> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PaginationTool<MoviesArray> paginationTool;

    @Inject
    protected Context context;
    @Inject
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular(RecyclerView recyclerView, FrameLayout layout, int primaryIndex) {
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getPopular(page), layout)
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable(primaryIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items ->
                        getView().onMoviesLoaded(items.getResults())));
    }

    public void getTopRated(RecyclerView recyclerView, FrameLayout layout, int primaryIndex) {
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getTopRated(page), layout)
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable(primaryIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> getView().onMoviesLoaded(items.getResults())));
    }

    public void getFavorites() {
        MoviesDbHelper helper = new MoviesDbHelper(context);
        List<SingleMovie> movies = helper.getSavedMovies();
        if (movies.size() == 0) {
            getView().hasEmptyFavoritesList();
        } else {
            getView().onMoviesLoaded(movies);
        }
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
