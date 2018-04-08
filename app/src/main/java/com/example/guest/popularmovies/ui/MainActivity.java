package com.example.guest.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;
import com.example.guest.popularmovies.mvp.model.SingleMovie;

import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_FAVORITES;
import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_POPULAR;
import static com.example.guest.popularmovies.ui.MainFragment.SORT_ORDER_TOP_RATED;

public class MainActivity extends BaseActivity implements MainFragment.Callbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        fragment = (MainFragment) manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new MainFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_masterdetail;
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
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_POPULAR);
                return true;
            case R.id.action_top_rated:
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_TOP_RATED);
                return true;
            case R.id.action_favorites:
                fragment.doWorkOnChangingSortOrder(SORT_ORDER_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(SingleMovie movie) {
        Log.d(TAG, movie.getTitle() + " element clicked.");

        if (findViewById(R.id.twopane_detail_container) == null) {
            startActivity(DetailActivity.newIntent(this, movie));
        } else {
            Fragment detailFragment = DetailFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.twopane_detail_container, detailFragment)
                    .commit();
        }
    }
}