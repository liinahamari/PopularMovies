package com.example.guest.popularmovies.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.ui.DetailFragment;
import com.example.guest.popularmovies.utils.DbOperations;
import com.example.guest.popularmovies.utils.LikeButtonColorChanger;
import com.example.guest.popularmovies.utils.RxThreadManager;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by l1maginaire on 3/1/18.
 **/

public abstract class BaseActivity extends AppCompatActivity implements DetailFragment.Callbacks {
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        setContentView(getContentView());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = getMainFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public void onLikeClicked(SingleMovie movie, FloatingActionButton fab) {
        if (movie.isInFavorites() == 0) {
            fab.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> DbOperations.insert(movie, this))
                    .compose(RxThreadManager.manageSingle())
                    .subscribe(uri -> {
                        movie.setInFavorites(1);
                        LikeButtonColorChanger.change(fab, this, 1);
                        fab.setClickable(true);
                    }));
        } else {
            fab.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> DbOperations.delete(movie.getTitle(), this))
                    .compose(RxThreadManager.manageSingle())
                    .subscribe(rowsDeleted -> {
                        if (rowsDeleted != 0) {
                            movie.setInFavorites(0);
                            LikeButtonColorChanger.change(fab, this, 0);
                        }
                        fab.setClickable(true);
                    }));
        }
    }

    protected abstract int getContentView();

    protected abstract Fragment getMainFragment();
}