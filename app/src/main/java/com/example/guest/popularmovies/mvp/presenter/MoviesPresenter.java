package com.example.guest.popularmovies.mvp.presenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.guest.popularmovies.api.MovDbApi;
import com.example.guest.popularmovies.base.BasePresenter;
import com.example.guest.popularmovies.db.MoviesDbHelper;
import com.example.guest.popularmovies.mvp.model.MoviesArray;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.pagination.PaginationTool;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_BACKDROP_PATH;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_MOV_ID;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_ORIGINAL_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_OVERVIEW;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_POPULARITY;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_POSTER_PATH;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_RELEASE_DATE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_VOTE_AVERAGE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_VOTE_COUNT;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_GENRE_IDS;

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

    /*public void writeToDb(Context context, List<SingleMovie> movieList) {
        Single.fromCallable(() -> {
            ContentResolver contentResolver = context.getContentResolver();
            int rowsNewlyCreated = contentResolver.bulkInsert(CONTENT_URI, MoviesPresenter.this.makeContentValues(movieList));
            return rowsNewlyCreated;
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }*/
    public void getPopular(List<SingleMovie> movies){

    }

    public void getPopular(RecyclerView recyclerView) {
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getPopular(page))
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread()) //todo subscribeon?
                .subscribe(items ->
                        getView().onMoviesLoaded(items.getResults())));
    }


    public void getTopRated(RecyclerView recyclerView) {
        paginationTool = PaginationTool.buildPagingObservable(recyclerView,
                page -> apiService.getTopRated(page))
                .build();
        compositeDisposable.clear();
        compositeDisposable.add(paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> getView().onMoviesLoaded(items.getResults())));
    }

    public void getFavorites(Context context) {
        MoviesDbHelper helper = new MoviesDbHelper(context);
        List<SingleMovie> movies = helper.getSavedMovies();
        getView().onMoviesLoaded(movies);
    }

    public void unsubscribe() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
