package com.example.guest.popularmovies.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.utils.MakeContentValues;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;
import static com.example.guest.popularmovies.utils.MakeContentValues.makeContentValues;

public class DetailActivity extends BaseActivity implements DetailFragment.Callbacks{
    public static final String IDENTIFICATION = "extra_movie";
    private SingleMovie movie;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        getSupportActionBar().hide();
        movie = getIntent().getParcelableExtra(IDENTIFICATION);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = DetailFragment.newInstance(movie);
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context context, SingleMovie movie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(IDENTIFICATION, movie);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
