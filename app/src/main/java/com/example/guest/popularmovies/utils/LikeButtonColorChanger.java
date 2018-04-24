package com.example.guest.popularmovies.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;

import com.example.guest.popularmovies.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Created by l1maginaire on 4/24/18.
 */

public class LikeButtonColorChanger {
    public static void change(FloatingActionButton fab, Context context, int isFavorite) {
        int color = (isFavorite == 0) ? R.color.lightLight : R.color.colorAccent;
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            fab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(color)));
        } else {
            fab.getBackground().setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.MULTIPLY);
        }
    }
}
