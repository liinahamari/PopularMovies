package com.example.guest.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l1maginaire on 4/7/18.
 */

public class ReviewFragment extends Fragment {
    @BindView(R.id.pagers_review)
    protected TextView reviewTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt("position");
        View view = inflater.inflate(R.layout.pager_item_review, container, false);
        ButterKnife.bind(this, view);

        if (position == 0) {
            reviewTv.setText("00000000000000");
        }
        if (position == 1) {
            reviewTv.setText("11111111111");
        }
        if (position == 2) {
            reviewTv.setText("222222222222");
        }
        if (position == 3) {
            reviewTv.setText("333333333333");
        }
        return view;
    }
}
