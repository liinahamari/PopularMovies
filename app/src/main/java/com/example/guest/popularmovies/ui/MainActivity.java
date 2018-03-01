package com.example.guest.popularmovies.ui;

import android.support.v4.app.Fragment;

import com.example.guest.popularmovies.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MainFragment();
    }
}
