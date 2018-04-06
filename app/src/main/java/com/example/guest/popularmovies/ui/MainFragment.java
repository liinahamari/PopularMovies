package com.example.guest.popularmovies.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.adapters.MovieListAdapter;
import com.example.guest.popularmovies.base.BaseFragment;
import com.example.guest.popularmovies.di.components.DaggerMovieComponent;
import com.example.guest.popularmovies.di.modules.MovieModule;
import com.example.guest.popularmovies.mvp.model.SingleMovie;
import com.example.guest.popularmovies.mvp.presenter.MoviesPresenter;
import com.example.guest.popularmovies.mvp.view.MainView;
import com.example.guest.popularmovies.utils.NetworkChecker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l1maginaire on 4/6/18.
 */

public class MainFragment extends BaseFragment implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String LAST_SORT_ORDER = "last_chosen_sort_order";
    public static final String SORT_ORDER_POPULAR = "order_popular";
    public static final String SORT_ORDER_TOP_RATED = "order_top_rated";
    public static final String SORT_ORDER_FAVORITES = "order_favorites";
    private static final String LAST_POSITION = "last_position";
    private static final String SAVED_LIST = "list";

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            lastVisiblePosition = savedInstanceState.getInt(LAST_POSITION);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, v);
        resolveDaggerDependencies();
        init();
        setupAdapter();
        if (savedInstanceState != null) {
            onMoviesLoaded(savedInstanceState.getParcelableArrayList(SAVED_LIST));
            recyclerView.scrollToPosition(lastVisiblePosition);
        } else {
            loadNews();
        }
        return v;
    }

    public void get(String choice) {
        switch (choice) {
            case SORT_ORDER_POPULAR:
                preferences.edit().putString(LAST_SORT_ORDER, choice).apply();
                presenter.getPopular(recyclerView);
                break;
            case SORT_ORDER_TOP_RATED:
                preferences.edit().putString(LAST_SORT_ORDER, choice).apply();
                presenter.getTopRated(recyclerView);
                break;
            case SORT_ORDER_FAVORITES:
                preferences.edit().putString(LAST_SORT_ORDER, choice).apply();
                presenter.getFavorites(getActivity());
                break;
            default:
                throw new IllegalArgumentException("There's only 3 options to go...");
        }
    }

    private void loadNews() {
        if (networkChecker.isNetAvailable(getActivity())) {
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
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        adapter = new MovieListAdapter(getLayoutInflater(), getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void init() {
        savedList = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    protected void resolveDaggerDependencies() {
        DaggerMovieComponent.builder()
                .applicationComponent(getApplicationComponent(getActivity()))
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
    public void onDetach() {
        super.onDetach();
        presenter.getPopular(recyclerView); //todo: or onDestroy?
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
                presenter.getFavorites(getActivity());
                break;
            default:
                throw new IllegalArgumentException("There's only 3 options to go...");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        Log.d("POSITION", String.valueOf(lastVisiblePosition));
        outState.putInt(LAST_POSITION, lastVisiblePosition);
        outState.putParcelableArrayList(SAVED_LIST, savedList);
    }
}
