package com.example.guest.popularmovies.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class MainActivity extends BaseActivity implements MainFragment.Callbacks, DetailFragment.Callbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private int position;
    private MainFragment mainFragment;

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
                mainFragment.doWorkOnChangingSortOrder(SORT_ORDER_POPULAR);
                return true;
            case R.id.action_top_rated:
                mainFragment.doWorkOnChangingSortOrder(SORT_ORDER_TOP_RATED);
                return true;
            case R.id.action_favorites:
                mainFragment.doWorkOnChangingSortOrder(SORT_ORDER_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClicked(SingleMovie movie, int position) {
        Log.d(TAG, movie.getTitle() + " element clicked.");

        this.position = position;

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

    protected void setFab(FloatingActionButton fab) {
        mainFragment.setFab(fab, position);
    }

    @Override
    protected Fragment getMainFragment() {
        mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public void onLikeClicked(SingleMovie movie, FloatingActionButton floatingButton) {
        super.onLikeClicked(movie, floatingButton);
        mainFragment.notifyItemChanged(position);
    }
}