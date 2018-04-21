package com.example.guest.popularmovies.mvp.presenter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.guest.popularmovies.adapters.FavoritesAdapter;
import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

/**
 * Created by l1maginaire on 2/25/18.
 */

public class MoviesPresenter extends BasePresenter<MainView> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PaginationTool<MoviesArray> paginationTool;
    private Cursor cursor;

    @Inject
    protected Context context;
    @Inject
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular(RecyclerView recyclerView, FrameLayout layout, int primaryIndex) {
        if (paginationTool != null)
            paginationTool.dispose();
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getPopular(page), layout)
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable(primaryIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> getView().onMoviesLoaded(items.getResults())));
    }

    public void getTopRated(RecyclerView recyclerView, FrameLayout layout, int primaryIndex) {
        if (paginationTool != null)
            paginationTool.dispose();
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getTopRated(page), layout)
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable(primaryIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> getView().onMoviesLoaded(items.getResults())));
    }

    public void getFavorites(RecyclerView recyclerView, FrameLayout emptyFavoritesFrame, LayoutInflater layoutInflater) {
        if (paginationTool != null)
            paginationTool.dispose();
        Single.fromCallable(() -> context.getContentResolver()
                .query(CONTENT_URI, null, null, null, null))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(cursor -> {
                    this.cursor = cursor;
                    if (cursor.getCount() == 0) {
                        emptyFavoritesFrame.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setAdapter(new FavoritesAdapter(context, cursor, emptyFavoritesFrame, layoutInflater));
                    }
                });
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
        if (cursor != null)
            cursor.close();
    }
}
