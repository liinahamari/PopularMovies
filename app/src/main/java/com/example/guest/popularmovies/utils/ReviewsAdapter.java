package com.example.guest.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.popularmovies.R;
import com.example.guest.popularmovies.mvp.model.reviews.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l1maginaire on 4/4/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {//todo check simple adapter insted of creating class
    private List<Review> reviews;
    private Context context;

    public ReviewsAdapter(Context context) {
        this.context = context;
        reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_item, parent, false);
        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.review.setText(reviews.get(position).getContent());
    }

    public void addReviews(List<Review> reviews2) {
        if (reviews2.size() > 0) {
            reviews.addAll(reviews2);
        } else {
            Review review = new Review();
            review.setContent("No reviews found...");
            reviews.add(review);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (reviews == null) ? 0 : reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.single_review)
        TextView review;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}