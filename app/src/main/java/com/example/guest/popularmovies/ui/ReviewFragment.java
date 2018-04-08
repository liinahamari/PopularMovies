package com.example.guest.popularmovies.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

/**
 * Created by l1maginaire on 4/7/18.
 */

public class ReviewFragment extends Fragment {
    public static final String REVIEW_DATA = "data";
    public static final String REVIEW_AUTHOR = "author";
    @BindView(R.id.single_review)
    protected TextView reviewTv;
    @BindView(R.id.load_more_label)
    protected TextView loadMoreTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.reviews_item, container, false);
        ButterKnife.bind(this, view);
        String message = getArguments().getString(REVIEW_DATA) + "\n\n by: " + getArguments().getString(REVIEW_AUTHOR);
        reviewTv.setText(message);
        reviewTv.post(() -> {
            if (reviewTv.getLineCount() > 10) {
                loadMoreTv.setVisibility(View.VISIBLE);
                loadMoreTv.setOnClickListener(v -> {
                    AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                    ad.setCancelable(true);
                    ad.setMessage(message);
                    ad.setButton(BUTTON_NEGATIVE, "Close", (dialog, which) -> dialog.dismiss());
                    ad.show();
                    TextView textView = (TextView) ad.getWindow().findViewById(android.R.id.message);
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "oregon_ldo.ttf");
                    textView.setTypeface(face);
                    textView.setTextSize(22);
                });
            }
        });
        return view;
    }


}
