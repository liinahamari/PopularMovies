package com.example.guest.popularmovies.ui;

import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.utils.MakeContentValues;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_FAVORITES;
import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_POPULAR;
import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_TOP_RATED;
import static com.example.guest.popularmovies.utils.MakeContentValues.makeContentValues;

public class MainActivity extends BaseActivity implements MainFragment.Callbacks, DetailFragment.Callbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainFragment fragment;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        FragmentManager manager = getSupportFragmentManager();
        fragment = (MainFragment) manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new MainFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_POPULAR);
                return true;
            case R.id.action_top_rated:
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_TOP_RATED);
                return true;
            case R.id.action_favorites:
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(SingleMovie movie) {
        Log.d(TAG, movie.getTitle() + " element clicked.");

        if (findViewById(R.id.twopane_detail_container) == null) {
            startActivity(DetailActivity.newIntent(this, movie));
        } else {
            Fragment detailFragment = DetailFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.twopane_detail_container, detailFragment)
                    .commit();
        }
    }

    public void onLikeClicked(SingleMovie movie, FloatingActionButton floatingButton) {
        if (movie.isInFavorites() == 0) {
            floatingButton.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> {
                return getContentResolver().insert(CONTENT_URI, (makeContentValues(movie))); //todo class optimization
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(uri -> {
                        movie.setInFavorites(1);
                        Toast.makeText(this, movie.getTitle() + " added to Favorites!", Toast.LENGTH_SHORT).show();
                        floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        floatingButton.setClickable(true);
                    }));
        } else {
            floatingButton.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> {
                ContentResolver contentResolver = this.getContentResolver();
                return contentResolver.delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                        new String[]{(makeContentValues(movie)).getAsString(COLUMN_TITLE)});
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(rowsDeleted -> {
                        movie.setInFavorites(0);
                        Toast.makeText(this, movie.getTitle() + " removed from Favorites!", Toast.LENGTH_SHORT).show();
                        floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightLight))); //todo check versions <21
                        floatingButton.setClickable(true);
                    }));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable!=null)
            compositeDisposable.dispose();
    }
}