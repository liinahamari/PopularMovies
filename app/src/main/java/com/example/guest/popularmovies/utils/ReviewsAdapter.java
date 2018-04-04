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

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_item, parent, false);
        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review.setText(review.getContent());
    }

    public void addReviews(List<Review> reviews2) {
        reviews.addAll(reviews2);
        notifyDataSetChanged();
    }

    public void clearItems() {
        reviews.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (reviews == null) ? 0 : reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.single_review)
        protected TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            review = (TextView) itemView.findViewById(R.id.single_review);
        }
    }
}