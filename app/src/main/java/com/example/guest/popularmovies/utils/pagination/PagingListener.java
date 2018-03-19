package com.example.guest.popularmovies.utils.pagination;

/**
 * Created by l1maginaire on 3/14/18.
 */

import io.reactivex.Observable;

public interface PagingListener<T> {
    Observable<T> onNextPage(Integer page);
}
