package com.example.guest.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.guest.popularmovies.dagger.components.DaggerMovieComponent;
import com.example.guest.popularmovies.dagger.components.MovieComponent;
import com.example.guest.popularmovies.dagger.modules.ContextModule;
import com.example.guest.popularmovies.data.model.MoviesArray;
import com.example.guest.popularmovies.data.model.SingleMovie;
import com.example.guest.popularmovies.interfaces.MovieDbApi;
import com.example.guest.popularmovies.utils.Adapter;
import com.example.guest.popularmovies.utils.ArrayAdapter;
import com.example.guest.popularmovies.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by guest on 2/19/18.
 */

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Adapter adapter;
    private List<SingleMovie> resultList;
    private MovieDbApi movieDbApi;
    private CompositeDisposable compositeDisposable;
    private GridView gridView;

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieComponent daggerMovieComponent = DaggerMovieComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .build();
        movieDbApi = daggerMovieComponent.getMovieService();
        compositeDisposable = new CompositeDisposable();
        fetchData();
    }

    private void fetchData() {
        compositeDisposable.add(movieDbApi.getDefault()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    resultList = results.getResults();
                    setupAdapter();
                }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_gallery, container, false);
        gridView = view.findViewById(R.id.gridView);
//        recyclerView = (RecyclerView) view.findViewById(R.id.posts_recycle_view);
//        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    /*private void setupAdapter() {
        if (isAdded()) {
            adapter = new Adapter(resultList, getContext());
            recyclerView.setAdapter(adapter);
        }
    }*/

    void setupAdapter() {
        if (getActivity() == null || gridView == null) return;

        if (resultList != null) {
            gridView.setAdapter(new ArrayAdapter(resultList, getContext()));
        } else {
            gridView.setAdapter(null);
        }
    }
}

