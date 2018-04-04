/*
package com.example.guest.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guest.popularmovies.R;

*/
/**
 * Created by l1maginaire on 4/3/18.
 *//*


public class FavoritesAdapter extends RecyclerView.MovieListAdapter<FavoritesAdapter.FavoritesHolder> {
    private Cursor mCursor;
    private Context context;

    public FavoritesAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.mCursor = cursor;
    }

    @Override
    public FavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new FavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class FavoritesHolder extends RecyclerView.ViewHolder {
*/
/*views*//*


        public FavoritesHolder(View itemView) {
            super(itemView);
        }
    }
}*/
