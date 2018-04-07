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

public class DetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {
    public static final String IDENTIFICATION = "extra_movie";
    @BindView(R.id.mytoolbar)
    protected Toolbar toolbar;
    @BindView(R.id.my_appbar)
    protected AppBarLayout appbar;
    @BindView(R.id.fab)
    protected FloatingActionButton floatingButton;
    @BindView(R.id.my_collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.d_poster)
    protected ImageView posterIv;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private SingleMovie movie;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        movie = getIntent().getParcelableExtra(IDENTIFICATION);
        ButterKnife.bind(this);

        Picasso.with(this).load("http://image.tmdb.org/t/p/original/" + movie.getPosterPath())
                .into(posterIv);

        setActionBarView();
        setupListeners();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.empty_space);
        if (fragment == null) {
            fragment = new DetailFragment();
            manager.beginTransaction()
                    .add(R.id.empty_space, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context context, SingleMovie movie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(IDENTIFICATION, movie);
        return intent;
    }

    private void setActionBarView() {
        getSupportActionBar().hide();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        appbar.addOnOffsetChangedListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail;
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(floatingButton).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(floatingButton).scaleY(1).scaleX(1).start();
            }
        }
    }

    private void setupListeners() {
        floatingButton.setBackgroundTintList((movie.isInFavorites() != 0) ?
                (ColorStateList.valueOf(getResources().getColor(R.color.colorAccent))) : //todo check <21
                ColorStateList.valueOf(getResources().getColor(R.color.lightLight)));
        floatingButton.setOnClickListener(v -> {
            if (movie.isInFavorites() == 0) {
                floatingButton.setClickable(false);
                compositeDisposable.add(Single.fromCallable(() -> {
                    return getContentResolver().insert(CONTENT_URI, (new MakeContentValues().makeContentValues(movie))); //todo class optimization
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
                            new String[]{(new MakeContentValues().makeContentValues(movie)).getAsString(COLUMN_TITLE)});
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
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
