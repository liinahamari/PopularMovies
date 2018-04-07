package com.example.guest.popularmovies.ui.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.guest.popularmovies.ui.ReviewFragment;

/**
 * Created by l1maginaire on 4/7/18.
 */

public class ReviewsPagerAdapter extends FragmentStatePagerAdapter {
    public ReviewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}