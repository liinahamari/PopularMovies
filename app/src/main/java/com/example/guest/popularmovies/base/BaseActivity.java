package com.example.guest.popularmovies.base;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.ui.DetailFragment;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.utils.MakeContentValues.makeContentValues;

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

    public void onLikeClicked(SingleMovie movie, FloatingActionButton floatingButton) {
        if (movie.isInFavorites() == 0) {
            floatingButton.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> getContentResolver().insert(CONTENT_URI, (makeContentValues(movie))))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(uri -> {
                        movie.setInFavorites(1);
                        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                            floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        } else {
                            floatingButton.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
                        }
                        floatingButton.setClickable(true);
                    }));
        } else {
            floatingButton.setClickable(false);
            compositeDisposable.add(Single.fromCallable(() -> getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                    new String[]{(makeContentValues(movie)).getAsString(COLUMN_TITLE)}))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(rowsDeleted -> {
                        movie.setInFavorites(0);
                        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                            floatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightLight)));
                        } else {
                            floatingButton.getBackground().setColorFilter(getResources().getColor(R.color.lightLight), PorterDuff.Mode.MULTIPLY);
                        }
                        floatingButton.setClickable(true);
                    }));
        }
    }

    protected abstract int getContentView();

    protected abstract Fragment getMainFragment();
}