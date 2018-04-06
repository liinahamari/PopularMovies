package com.example.guest.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.base.BaseActivity;

import static com.example.guest.popularmovies.ui.MainFragment.*;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MainFragment fragment;

    /*@Override //todo saveRetainInstanceState?
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
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getSupportFragmentManager();
        fragment = (MainFragment) manager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new MainFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
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
                fragment.get(SORT_ORDER_POPULAR);
                return true;
            case R.id.action_top_rated:
                fragment.get(SORT_ORDER_TOP_RATED);
                return true;
            case R.id.action_favorites:
                fragment.get(SORT_ORDER_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void doWorkOnChangingSortOrder(String sortOrder) {
//        savedList.clear();
        preferences.edit().putString(LAST_SORT_ORDER, sortOrder).apply();
        onClearItems();
        sortingSwitcher(sortOrder);
    }*/
}