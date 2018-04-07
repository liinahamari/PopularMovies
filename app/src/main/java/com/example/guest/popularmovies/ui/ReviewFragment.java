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
    public static final String REVIEW_DATA = "data";
    @BindView(R.id.single_review)
    protected TextView reviewTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.reviews_item, container, false);
        ButterKnife.bind(this, view);
        reviewTv.setText(getArguments().getString(REVIEW_DATA));
        return view;
    }
}
