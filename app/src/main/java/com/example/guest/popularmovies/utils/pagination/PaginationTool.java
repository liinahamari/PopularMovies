package com.example.guest.popularmovies.utils.pagination;

/**
 * Created by l1maginaire on 3/14/18.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaginationTool<T> {
    private static final int ATTEMPTS_TO_RETRY_LOADING = 3;

    private RecyclerView recyclerView;
    private PagingListener<T> pagingListener;

    private PaginationTool() {}

    public Observable<T> getPagingObservable() {
        return getScrollObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.io())
                .switchMap(offset -> PaginationTool.this.getPagingObservable(pagingListener.onNextPage(offset)));
    }

    private Observable<Integer> getScrollObservable() {
        return Observable.create(subscriber -> {
            final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!subscriber.isDisposed()) {
                        int position = getLastVisibleItemPosition();
                        int updatePosition = recyclerView.getAdapter().getItemCount() - 1 - (20 / 2);
                        if (position >= updatePosition) {
                            int actualPage = (recyclerView.getAdapter().getItemCount() / 20);
                            subscriber.onNext(++actualPage);
                        }
                    }
                }
            };
            recyclerView.addOnScrollListener(scrollListener);
            subscriber.setDisposable(new Disposable() {
                @Override
                public void dispose() {
                    recyclerView.removeOnScrollListener(scrollListener);
                }

                @Override
                public boolean isDisposed() {
                    return false;
                }
            });
            if (recyclerView.getAdapter().getItemCount() == 0) {
                subscriber.onNext(1);
            }
        });
    }

    private int getLastVisibleItemPosition() {
        Class recyclerViewLMClass = recyclerView.getLayoutManager().getClass();
        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            return linearLayoutManager.findLastVisibleItemPosition();
        } else {
            throw new PagingException("Unknown LayoutManager class: " + recyclerViewLMClass.toString());
        }
    }

    private Observable<T> getPagingObservable(Observable<T> observable) {
        return observable
                .retry(ATTEMPTS_TO_RETRY_LOADING)
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                });
    }

    public static <T> Builder<T> buildPagingObservable(RecyclerView recyclerView, PagingListener<T> pagingListener) {
        return new Builder<>(recyclerView, pagingListener);
    }

    public static class Builder<T> {

        private RecyclerView recyclerView;
        private PagingListener<T> pagingListener;

        private Builder(RecyclerView recyclerView, PagingListener<T> pagingListener) {
            if (recyclerView == null) {
                throw new PagingException("Null recyclerView");
            }
            if (recyclerView.getAdapter() == null) {
                throw new PagingException("Null recyclerView adapter");
            }
            if (pagingListener == null) {
                throw new PagingException("Null pagingListener");
            }
            this.recyclerView = recyclerView;
            this.pagingListener = pagingListener;
        }

        public PaginationTool<T> build() {
            PaginationTool<T> paginationTool = new PaginationTool<>();
            paginationTool.recyclerView = this.recyclerView;
            paginationTool.pagingListener = pagingListener;
            return paginationTool;
        }
    }
}
