package com.example.guest.popularmovies.mvp.presenter;

import android.support.v7.widget.RecyclerView;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.Adapter;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;
import com.example.guest.popularmovies.utils.pagination.PagingListener;

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
    protected MovDbApi apiService;

    @Inject
    public MoviesPresenter() {
    }

    public void getPopular(RecyclerView recyclerView) {
        Adapter adapter = (Adapter) (recyclerView.getAdapter());
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                new PagingListener<MoviesArray>() {
                    @Override
                    public Observable<MoviesArray> onNextPage(int page) { //todo starts with zero
                        return apiService.getPopular(page);
                    }
                })
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    adapter.addMovies(items.getResults());
                    adapter.notifyItemInserted(adapter.getItemCount() - items.getResults().size());
                }));
    }


    public void getTopRated(RecyclerView recyclerView) {
        Adapter adapter = (Adapter) (recyclerView.getAdapter());
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getTopRated(page))
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    adapter.addMovies(items.getResults());
                    adapter.notifyItemInserted(adapter.getItemCount() - items.getResults().size());
                }));
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
