package com.example.guest.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.guest.popularmovies.data.model.SingleMovie;

import java.util.ArrayList;

/**
 * Created by guest on 2/19/18.
 */

public class MainFragment extends Fragment{
    private static final String API_KEY = BuildConfig.API_KEY;

    private GridView gridView;
    private ArrayList<SingleMovie> resultList;
//    private ThumbnailDownloader<> mThumbnailThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
