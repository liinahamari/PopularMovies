package com.example.guest.popularmovies.mvp.presenter;

import android.support.v7.widget.RecyclerView;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.Adapter;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by l1maginaire on 2/25/18.
 */

public class MoviesPresenter extends BasePresenter<MainView> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable disposable;

    @Inject
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular(RecyclerView recyclerView, Adapter adapter) {
        /*Observable<MoviesArray> observable = apiService.getPopular(page);
        compositeDisposable.add(subscribe(observable, response -> {
            List<SingleMovie> movies = response.getResults();
            getView().onClearItems();
            getView().onMoviesLoaded(movies);
        }, Throwable::printStackTrace, () -> {
            // todo: onComplete logging
        }));*/

        PaginationTool<MoviesArray> paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getPopular(++page))
                .build();

        disposable = paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoviesArray>() {
                    @Override
                    public void accept(MoviesArray items) throws Exception {
                        adapter.addMovies(items.getResults());
                        adapter.notifyItemInserted(adapter.getItemCount() - items.getResults().size());
                    }
                });
    }


    public void getTopRated(int page) {
        Observable<MoviesArray> observable = apiService.getTopRated(page);
        compositeDisposable.add(subscribe(observable, response -> {
            List<SingleMovie> movies = response.getResults();
            getView().onClearItems();
            getView().onMoviesLoaded(movies);
        }, Throwable::printStackTrace, () -> {
            // todo: onComplete logging
        }));
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
