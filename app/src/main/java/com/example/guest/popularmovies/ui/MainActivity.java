package com.example.guest.popularmovies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.db.MoviesDbHelper;
import com.example.guest.popularmovies.di.components.DaggerMovieComponent;
import com.example.guest.popularmovies.di.modules.MovieModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.Adapter;
import com.example.guest.popularmovies.utils.NetworkChecker;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView {
    private static final String LAST_POSITION = "last_position";

    @Inject
    protected MoviesPresenter presenter;
    @Inject
    protected NetworkChecker networkChecker;

    @BindView(R.id.mov_recycler)
    protected RecyclerView recyclerView;
    @BindView(R.id.errorLayout)
    protected FrameLayout errorLayout;
    @BindView(R.id.btn_repeat)
    protected Button repeatButton;

    private Adapter adapter;
    private int lastFirstVisiblePosition = 1;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setupAdapter();
        loadNews();
    }

    private void loadNews() {
        if (networkChecker.isNetAvailable(this)) {
            errorLayout.setVisibility(View.INVISIBLE);
            presenter.getPopular(recyclerView); //todo sharedpreferences
        } else {
            errorLayout.setVisibility(View.VISIBLE);
            repeatButton.setOnClickListener(v -> loadNews());
        }
    }

    private void setupAdapter() {
        recyclerView.setHasFixedSize(true); //todo necessity
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        adapter = new Adapter(getLayoutInflater(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
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
                onClearItems();
                presenter.getPopular(recyclerView);
                return true;
            case R.id.action_top_rated:
                onClearItems();
                presenter.getTopRated(recyclerView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void resolveDaggerDependencies() {
        DaggerMovieComponent.builder()
                .applicationComponent(getApplicationComponent())
                .movieModule(new MovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onMoviesLoaded(List<SingleMovie> movies) {
        MoviesDbHelper dbHelper = new MoviesDbHelper(this); //todo to background thread
        dbHelper.addMovies(movies);
        adapter.addMovies(movies);
        //todo mapper to storage
        adapter.notifyItemInserted(adapter.getItemCount() - movies.size());
    }

    @Override
    public void onClearItems() {
        adapter.clearItems();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastFirstVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        Log.d("POSITION", String.valueOf(lastFirstVisiblePosition));
        outState.putInt(LAST_POSITION, lastFirstVisiblePosition);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("last_position", lastFirstVisiblePosition).apply();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastFirstVisiblePosition = savedInstanceState.getInt(LAST_POSITION);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lastFirstVisiblePosition = preferences.getInt("last_position", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }
}