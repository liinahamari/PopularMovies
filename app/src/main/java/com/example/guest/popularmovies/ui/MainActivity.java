package com.example.guest.popularmovies.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.di.components.DaggerMovieComponent;
import com.example.guest.popularmovies.di.modules.MovieModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.Adapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    protected MoviesPresenter presenter;

    @BindView(R.id.mov_recycler)
    protected RecyclerView recyclerView;

    private Adapter adapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setupAdapter();
        loadNews();
    }

    private void loadNews() {
        presenter.getPopular(recyclerView, adapter);
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
                setupAdapter(); //todo another way?
                presenter.getPopular(recyclerView, adapter);
                return true;
            case R.id.action_top_rated:
                setupAdapter();
                presenter.getTopRated(recyclerView, adapter);
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

    public void onMoviesLoaded(List<SingleMovie> movies) {
        adapter.addMovies(movies);
    }

    @Override
    public void onClearItems() {
        adapter.clearNews();
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }
}