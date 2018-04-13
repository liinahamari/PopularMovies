package com.example.guest.popularmovies.ui;

import android.app.Activity;
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
import android.widget.TextView;

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

import static android.view.View.VISIBLE;

/**
 * Created by l1maginaire on 4/6/18.
 */

public class MainFragment extends BaseFragment implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String SORT_ORDER_POPULAR = "order_popular";
    public static final String SORT_ORDER_TOP_RATED = "order_top_rated";
    public static final String SORT_ORDER_FAVORITES = "order_favorites";

    private static final String LAST_POSITION = "last_position";
    private static final String LAST_SORT_ORDER = "last_chosen_sort_order";
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
    @BindView(R.id.empty_favorites_frame)
    protected FrameLayout emptyFavoritesFrame;
    @BindView(R.id.error_msg_frame)
    protected FrameLayout errorMsgFrame;
    @BindView(R.id.error_msg)
    protected TextView errorMsg;

    private MovieListAdapter adapter;
    private int lastVisiblePosition = 0;
    private ArrayList<SingleMovie> savedList;
    private SharedPreferences preferences;
    private Callbacks callbacks;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged(); //todo: optimization, itemChanged
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        errorMsg.setOnClickListener(view -> presenter.getPopular(recyclerView, errorMsgFrame));

        setupAdapter();
        if (savedInstanceState != null) {
            onMoviesLoaded(savedInstanceState.getParcelableArrayList(SAVED_LIST));
            recyclerView.scrollToPosition(lastVisiblePosition);
        } else {
            loadNew();
        }
        return v;
    }

    private void loadNew() {
        if (networkChecker.isNetAvailable(getActivity())) {
            errorLayout.setVisibility(View.INVISIBLE);
            if (preferences.contains(LAST_SORT_ORDER)) {
                sortingSwitcher(preferences.getString(LAST_SORT_ORDER, SORT_ORDER_POPULAR));
            } else {
                sortingSwitcher(SORT_ORDER_POPULAR);
            }
        } else {
            errorLayout.setVisibility(VISIBLE);
            repeatButton.setOnClickListener(v -> loadNew());
        }
    }

    private void setupAdapter() {
        recyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                || getResources().getBoolean(R.bool.isTab)) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        adapter = new MovieListAdapter(getLayoutInflater(), getActivity(), callbacks);
        recyclerView.setAdapter(adapter);
    }

    private void sortingSwitcher(String sortOrder) {
        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                presenter.getPopular(recyclerView, errorMsgFrame);
                break;
            case SORT_ORDER_TOP_RATED:
                presenter.getTopRated(recyclerView, errorMsgFrame);
                break;
            case SORT_ORDER_FAVORITES:
                presenter.getFavorites();
                break;
            default:
                throw new IllegalArgumentException("There's only 3 options to go...");
        }
    }

    public void doWorkOnChangingSortOrder(String sortOrder) {
        emptyFavoritesFrame.setVisibility(View.GONE);
        savedList.clear();
        preferences.edit().putString(LAST_SORT_ORDER, sortOrder).apply();
        onClearItems();
        sortingSwitcher(sortOrder);
    }

    @Override
    protected void init() {
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
    public void hasEmptyFavoritesList() {
        emptyFavoritesFrame.setVisibility(VISIBLE);
    }

    @Override
    public void onClearItems() {
        adapter.clearItems();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        lastVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        Log.d(LAST_POSITION, ": " + String.valueOf(lastVisiblePosition));
        outState.putInt(LAST_POSITION, lastVisiblePosition);
        outState.putParcelableArrayList(SAVED_LIST, savedList); //API < 7.0 == 1 mb
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unsubscribe(); //todo: or onDestroy?
        callbacks = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            lastVisiblePosition = savedInstanceState.getInt(LAST_POSITION);
        }
    }

    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    public interface Callbacks {
        void onItemClicked(SingleMovie movie, int position);
    }
}
