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
import com.example.guest.popularmovies.di.components.DaggerMovieComponent;
import com.example.guest.popularmovies.di.modules.MovieModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.adapters.MovieListAdapter;
import com.example.guest.popularmovies.utils.NetworkChecker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LAST_POSITION = "last_visible_position";
    private static final String LAST_SORT_ORDER = "last_chosen_sort_order";
    private static final String SORT_ORDER_POPULAR = "order_popular";
    private static final String SORT_ORDER_TOP_RATED = "order_top_rated";
    private static final String SORT_ORDER_FAVORITES = "order_favorites";

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

    private MovieListAdapter adapter;
    private int lastVisiblePosition = 0;
    private ArrayList<SingleMovie> savedList;
    private SharedPreferences preferences;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        init();
        setupAdapter();
        if (savedInstanceState != null) {
            onMoviesLoaded(savedInstanceState.getParcelableArrayList("list"));
            recyclerView.scrollToPosition(lastVisiblePosition);
        } else {
            loadNews();
        }
    }

    private void init() {
        savedList = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void loadNews() {
        if (networkChecker.isNetAvailable(this)) {
            errorLayout.setVisibility(View.INVISIBLE);
            if (preferences.contains(LAST_SORT_ORDER)) {
                sortingSwitcher(preferences.getString(LAST_SORT_ORDER, SORT_ORDER_POPULAR));
            } else {
                sortingSwitcher(SORT_ORDER_POPULAR);//todo check default value with null in sharedPref (1st launch)
            }
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
        adapter = new MovieListAdapter(getLayoutInflater(), this);
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
                doWorkOnChangingSortOrder(SORT_ORDER_POPULAR);
                return true;
            case R.id.action_top_rated:
                doWorkOnChangingSortOrder(SORT_ORDER_TOP_RATED);
                return true;
            case R.id.action_favorites:
                doWorkOnChangingSortOrder(SORT_ORDER_FAVORITES);
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
        savedList.addAll(movies);
        adapter.addMovies(movies);
        adapter.notifyItemInserted(adapter.getItemCount() - movies.size());
    }

    @Override
    public void onClearItems() {
        adapter.clearItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        Log.d("POSITION", String.valueOf(lastVisiblePosition));
        outState.putInt(LAST_POSITION, lastVisiblePosition);
        outState.putParcelableArrayList("list", savedList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastVisiblePosition = savedInstanceState.getInt(LAST_POSITION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    private void doWorkOnChangingSortOrder(String sortOrder) {
        savedList.clear();
        preferences.edit().putString(LAST_SORT_ORDER, sortOrder).apply();
        onClearItems();
        sortingSwitcher(sortOrder);
    }

    private void sortingSwitcher(String sortOrder) {
        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                presenter.getPopular(recyclerView);
                break;
            case SORT_ORDER_TOP_RATED:
                presenter.getTopRated(recyclerView);
                break;
            case SORT_ORDER_FAVORITES:
                presenter.getFavorites(this);
                break;
            default:
                throw new IllegalArgumentException("There's only 3 options to go...");
        }
    }
}