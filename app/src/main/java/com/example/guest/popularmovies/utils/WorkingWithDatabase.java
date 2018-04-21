/*
package com.example.guest.popularmovies.utils;

import android.content.Context;

import com.example.guest.popularmovies.mvp.model.SingleMovie;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.guest.popularmovies.db.MoviesContract.Entry.COLUMN_TITLE;
import static com.example.guest.popularmovies.db.MoviesContract.Entry.CONTENT_URI;

*/
/**
 * Created by guest2 on 4/21/2018.
 *//*


public class WorkingWithDatabase {
    public static void delete(Context context, List<SingleMovie> movies, int position, ){
        Single.fromCallable(() -> context.getContentResolver().delete(CONTENT_URI, COLUMN_TITLE + " = ?",
                new String[]{(movies.get(position).getTitle())}))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(rowsDeleted -> bookmarkCallback(movies.get(position), 0, holder, position));
    }

}
*/
